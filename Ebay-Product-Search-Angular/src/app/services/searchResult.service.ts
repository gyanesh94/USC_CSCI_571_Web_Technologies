import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';

import { SearchData } from '../models/searchData.model';
import { GeoLocationService } from './geoLocation.service';
import { LoggingService } from './logging.service';

@Injectable()
export class SearchResultService {
  private searchData: SearchData = new SearchData();
  private currentResultIsValid = false;

  constructor(
    private geoLocationService: GeoLocationService,
    private loggingService: LoggingService
  ) { }

  setData(newData: SearchData) {
    this.searchData.category = newData.category;
    this.searchData.keyword = newData.keyword;
    if (newData.distance === null) {
      this.searchData.distance = '10';
    } else {
      this.searchData.distance = newData.distance;
    }
    this.searchData.condition.conditionNew = newData.condition.conditionNew;
    this.searchData.condition.conditionUnspecified = newData.condition.conditionUnspecified;
    this.searchData.condition.conditionUsed = newData.condition.conditionUsed;
    this.searchData.here = newData.here;
    if (newData.here === 'here') {
      this.searchData.zipCode = this.geoLocationService.getCurrentZipCode();
    } else {
      this.searchData.zipCode = newData.zipCode;
    }
    this.searchData.shipping.freeShipping = newData.shipping.freeShipping;
    this.searchData.shipping.localPickupOnly = newData.shipping.localPickupOnly;

    this.currentResultIsValid = false;
    const params = new HttpParams().set('data', JSON.stringify(this.searchData))
    this.loggingService.logToConsole(params.toString());
  }
}
