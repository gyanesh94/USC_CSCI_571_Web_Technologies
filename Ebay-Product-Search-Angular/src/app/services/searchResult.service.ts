import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { SearchDataModel } from '../models/searchData.model';
import { GeoLocationService } from './geoLocation.service';
import { LoggingService } from './logging.service';
import { AppConfig } from '../app.config';
import { SearchResultModel } from '../models/searchResult.model';
import { StateService } from './state.service';
import { AppState } from '../models/appState.model';

@Injectable()
export class SearchResultService {
  private searchData: SearchDataModel = new SearchDataModel();
  private currentResultIsValid = false;
  private searchResult: { [id: string]: SearchResultModel } = {};

  private haveError = false;
  private errorMessage: string;

  constructor(
    private geoLocationService: GeoLocationService,
    private loggingService: LoggingService,
    private http: HttpClient,
    private appConfig: AppConfig,
    private stateService: StateService
  ) { }

  setData(newData: SearchDataModel) {
    this.searchData.category = newData.category;
    this.searchData.keyword = newData.keyword;
    if (newData.distance === null) {
      this.searchData.distance = '10';
    } else {
      this.searchData.distance = newData.distance;
    }
    this.searchData.condition.New = newData.condition.New;
    this.searchData.condition.Unspecified = newData.condition.Unspecified;
    this.searchData.condition.Used = newData.condition.Used;
    this.searchData.here = newData.here;
    if (newData.here === 'here') {
      this.searchData.zipcode = this.geoLocationService.getCurrentZipCode();
    } else {
      this.searchData.zipcode = newData.zipcode;
    }
    this.searchData.shipping.freeShipping = newData.shipping.freeShipping;
    this.searchData.shipping.localPickupOnly = newData.shipping.localPickupOnly;

    this.currentResultIsValid = false;
  }

  fetchResult() {
    if (this.currentResultIsValid) {
      return;
    }
    const params = new HttpParams().set('data', JSON.stringify(this.searchData));

    const apiEndPoint = this.appConfig.getApiEndPoint();
    const url = `${apiEndPoint}/search`;
    this.http.get(url, { params })
      .subscribe(
        (response: []) => {
          if (!response.length) {
            this.errorMessage = 'No records.';
            this.haveError = true;
            this.stateService.updateState(AppState.ResultComponent);
            return;
          }
          if (response.length && response[0].hasOwnProperty('error')) {
            this.errorMessage = response[0].error;
            this.haveError = true;
            this.stateService.updateState(AppState.ResultComponent);
            return;
          }
          for (const res of response) {
            const temp: SearchResultModel = Object.assign(new SearchResultModel(), res);
            this.searchResult[temp.itemId] = temp;
          }
          this.currentResultIsValid = true;
          this.stateService.updateState(AppState.ResultComponent);
        },
        _ => []
      );
  }
}
