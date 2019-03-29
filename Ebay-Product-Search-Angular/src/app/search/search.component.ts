import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

import { LoggingService } from '../services/logging.service';
import { GeoLocationService } from '../services/geoLocation.service';
import { ZipcodeSuggestionsService } from '../services/zipcodeSuggestions.service';
import { StateService } from '../services/state.service';
import { SearchResultService } from '../services/searchResult.service';
import { AppState } from '../models/appState.model';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  providers: [ZipcodeSuggestionsService]
})
export class SearchComponent implements OnInit, OnDestroy {
  searchFormGroup: FormGroup;
  zipcodeOptions: string[];
  subscriptions: Subscription[] = [];
  keywordHistory: string[] = [];
  showHistory: string[] = [];

  isSubmitValid = false;

  constructor(
    private loggingService: LoggingService,
    private geoLocationService: GeoLocationService,
    private zipcodeSuggestionsService: ZipcodeSuggestionsService,
    private stateService: StateService,
    private searchResultService: SearchResultService
  ) { }

  ngOnInit() {
    this.searchFormGroup = new FormGroup({
      keyword: new FormControl(null, Validators.required),
      category: new FormControl('all'),
      condition: new FormGroup({
        New: new FormControl(false),
        Used: new FormControl(false),
        Unspecified: new FormControl(false)
      }),
      shipping: new FormGroup({
        localPickupOnly: new FormControl(false),
        freeShipping: new FormControl(false)
      }),
      distance: new FormControl(null, Validators.min(0)),
      here: new FormControl('here'),
      zipcode: new FormControl({ value: null, disabled: true }, Validators.required)
    });

    this.subscriptions.push(
      this.searchFormGroup.get('zipcode').valueChanges
        .pipe(debounceTime(400))
        .subscribe(
          zipcode => {
            const regex = /^ +$/;
            if (zipcode !== null && zipcode.length > 0 && !regex.test(zipcode)) {
              this.zipcodeSuggestionsService.callGeoLocationApi(zipcode)
                .subscribe(
                  (response: []) => {
                    this.zipcodeOptions = response;
                  }
                );
            } else {
              this.zipcodeOptions = [];
            }
          }
        )
    );

    this.subscriptions.push(
      this.searchFormGroup.get('keyword').valueChanges.subscribe(
        keyword => {
          const result: string[] = [];
          for (const history of this.keywordHistory) {
            if (history.includes(keyword)) {
              result.push(history);
            }
          }
          this.showHistory = result;

          const regexpKeyword = /^ +$/;
          const keywordValue = this.searchFormGroup.get('keyword');
          if (keywordValue.value === null ||
            keywordValue.value.length === 0 ||
            regexpKeyword.test(keywordValue.value)
          ) {
            keywordValue.setErrors({ incorrect: true });
            this.isSubmitValid = false;
            return;
          } else {
            keywordValue.setErrors(null);
          }
          this.checkSubmitValid();
        }
      )
    );

    this.subscriptions.push(this.searchFormGroup.get('zipcode').valueChanges.subscribe(
      _ => {
        this.checkSubmitValid();
      }
    )
    );
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  locationMethodChange(value: string) {
    if (value === 'here') {
      this.searchFormGroup.get('zipcode').reset();
      this.searchFormGroup.get('zipcode').disable();
    } else if (value === 'zipcode') {
      this.searchFormGroup.get('zipcode').enable();
    }
    this.checkSubmitValid();
  }

  checkSubmitValid() {
    if (!this.searchFormGroup.valid) {
      this.isSubmitValid = false;
      return;
    }
    const regexpZipcode = /^\d{5}$/;
    if (this.searchFormGroup.get('here').value === 'zipcode' && !regexpZipcode.test(this.searchFormGroup.get('zipcode').value)) {
      this.isSubmitValid = false;
      return;
    }
    if (!this.geoLocationService.isGeoLocationReceived()) {
      this.isSubmitValid = false;
      return;
    }
    this.isSubmitValid = true;
  }

  onSubmit() {
    const keywordValue = this.searchFormGroup.get('keyword').value;
    if (!this.keywordHistory.includes(keywordValue)) {
      this.keywordHistory.push(keywordValue);
    }
    this.searchResultService.setData(this.searchFormGroup.value);
    this.stateService.updateState(AppState.ProgressBar);
    this.searchResultService.fetchResult();
  }

}
