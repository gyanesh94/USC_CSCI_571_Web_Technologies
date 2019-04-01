import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { SearchFormModel, emptySearchFormModel } from '../models/searchForm.model';
import { GeoLocationService } from './geoLocation.service';
import { LoggingService } from './logging.service';
import { AppConfig } from '../app.config';
import { SearchResultModel } from '../models/searchResult.model';
import { StateService } from './state.service';
import { AppState } from '../models/appState.model';

@Injectable()
export class SearchResultService {
  private searchForm: SearchFormModel = emptySearchFormModel();
  private searchResult: SearchResultModel[] = [];

  private haveError = false;
  private errorMessage = '';

  constructor(
    private geoLocationService: GeoLocationService,
    private loggingService: LoggingService,
    private http: HttpClient,
    private appConfig: AppConfig,
    private stateService: StateService
  ) { }

  setData(newData: SearchFormModel) {
    this.searchForm.category = newData.category;
    this.searchForm.keyword = newData.keyword;
    if (newData.distance === null) {
      this.searchForm.distance = '10';
    } else {
      this.searchForm.distance = newData.distance;
    }
    this.searchForm.condition.New = newData.condition.New;
    this.searchForm.condition.Unspecified = newData.condition.Unspecified;
    this.searchForm.condition.Used = newData.condition.Used;
    this.searchForm.here = newData.here;
    if (newData.here === 'here') {
      this.searchForm.zipcode = this.geoLocationService.getCurrentZipCode();
    } else {
      this.searchForm.zipcode = newData.zipcode;
    }
    this.searchForm.shipping.freeShipping = newData.shipping.freeShipping;
    this.searchForm.shipping.localPickupOnly = newData.shipping.localPickupOnly;
  }

  fetchResult() {
    const params = new HttpParams().set('data', JSON.stringify(this.searchForm));
    const apiEndPoint = this.appConfig.getApiEndPoint();
    const url = `${apiEndPoint}/search`;
    this.searchResult = [];
    this.http.get(url, { params })
      .subscribe(
        (response: SearchResultModel[]) => {
          if (!response.length) {
            this.errorMessage = 'No records.';
            this.haveError = true;
            this.stateService.updateState(AppState.ResultComponent);
            return;
          }
          this.haveError = false;
          this.errorMessage = '';
          this.searchResult = response;
          this.stateService.updateState(AppState.ResultComponent);
        },
        (error: HttpErrorResponse) => {
          this.haveError = true;
          this.errorMessage = error.error;
          this.stateService.updateState(AppState.ResultComponent);
        }
      );
  }

  getData() {
    return this.searchResult;
  }

  gotErrors() {
    if (!this.haveError) {
      if (!this.searchResult.length) {
        this.haveError = true;
        this.errorMessage = 'No Records';
      }
    }
    return this.haveError;
  }

  getErrorMessage() {
    return this.errorMessage;
  }

  clearData() {
    this.searchForm = emptySearchFormModel();
    this.searchResult = [];

    this.haveError = false;
    this.errorMessage = '';
  }
}
