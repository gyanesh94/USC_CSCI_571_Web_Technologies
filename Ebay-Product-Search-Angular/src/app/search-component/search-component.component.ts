import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

import { LoggingService } from '../services/logging.service';
import { GeoLocationService } from '../services/geoLocation.service';
import { ZipCodeSuggestionsService } from '../services/zipCodeSuggestions.service';

@Component({
  selector: 'app-search-component',
  templateUrl: './search-component.component.html',
  styleUrls: ['./search-component.component.css'],
  providers: [ZipCodeSuggestionsService]
})
export class SearchComponentComponent implements OnInit, OnDestroy {
  searchFormGroup: FormGroup;
  zipCodeOptions: string[];
  subscriptions: Subscription[] = [];
  keywordHistory: string[] = [];
  showHistory: string[] = [];

  isSubmitValid = false;

  constructor(
    private loggingService: LoggingService,
    private geoLocationService: GeoLocationService,
    private zipCodeSuggestionsService: ZipCodeSuggestionsService
  ) { }

  ngOnInit() {
    this.searchFormGroup = new FormGroup({
      keyword: new FormControl(null, Validators.required),
      category: new FormControl('all'),
      condition: new FormGroup({
        conditionNew: new FormControl(false),
        conditionUsed: new FormControl(false),
        conditionUnspecified: new FormControl(false)
      }),
      shipping: new FormGroup({
        localPickupOnly: new FormControl(false),
        freeShipping: new FormControl(false)
      }),
      distance: new FormControl(null, Validators.min(0)),
      here: new FormControl('here'),
      zipCode: new FormControl({ value: null, disabled: true }, Validators.required)
    });

    this.subscriptions.push(
      this.searchFormGroup.get('zipCode').valueChanges
        .pipe(debounceTime(700))
        .subscribe(
          zipCode => {
            const regex = /^ +$/;
            if (zipCode !== null && zipCode.length > 0 && !regex.test(zipCode)) {
              this.zipCodeSuggestionsService.callGeoLocationApi(zipCode)
                .subscribe(
                  (response: []) => {
                    this.zipCodeOptions = response;
                  }
                );
            } else {
              this.zipCodeOptions = [];
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
        }
      )
    );

    this.subscriptions.push(this.searchFormGroup.get('keyword').valueChanges.subscribe(
      _ => {
        const regexpKeyword = /^ +$/;
        const keywordValue = this.searchFormGroup.get('keyword');
        if (keywordValue !== null && regexpKeyword.test(keywordValue.value)) {
          keywordValue.setErrors({ incorrect: true });
          this.isSubmitValid = false;
          return;
        }
        this.checkSubmitValid();
      }
    )
    );

    this.subscriptions.push(this.searchFormGroup.get('zipCode').valueChanges.subscribe(
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
      this.searchFormGroup.get('zipCode').reset();
      this.searchFormGroup.get('zipCode').disable();
    } else if (value === 'zipCode') {
      this.searchFormGroup.get('zipCode').enable();
    }
    this.checkSubmitValid();
  }

  checkSubmitValid() {
    if (!this.searchFormGroup.valid) {
      this.isSubmitValid = false;
      return;
    }
    const regexpZipcode = /^\d{5}$/;
    if (this.searchFormGroup.get('here').value === 'zipCode' && !regexpZipcode.test(this.searchFormGroup.get('zipCode').value)) {
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
    this.loggingService.logToConsole(this.searchFormGroup.value);
  }

}
