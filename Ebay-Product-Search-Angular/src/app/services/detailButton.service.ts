import { Injectable } from '@angular/core';

import { SearchResultModel } from '../models/searchResult.model';
import { LoggingService } from './logging.service';
import { AppState } from '../models/appState.model';

@Injectable()
export class DetailButtonService {
  data: { [id in AppState]?: SearchResultModel } = {};

  constructor(
    private loggingService: LoggingService
  ) { }

  activateDetailButton(state: AppState) {
    if (state in this.data) {
      return true;
    }
    return false;
  }

  setDetailButton(product: SearchResultModel, state: AppState) {
    this.clearDetailButton(state);
    this.data[state] = product;
  }

  getProductResultData(state: AppState): SearchResultModel {
    return this.data[state];
  }

  clearDetailButton(state: AppState) {
    delete this.data[state];
  }
}
