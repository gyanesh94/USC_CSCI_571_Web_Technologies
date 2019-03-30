import { Injectable } from '@angular/core';

import { AppState } from '../models/appState.model';
import { StateService } from './state.service';
import { SearchResultModel } from '../models/searchResult.model';
import { LoggingService } from './logging.service';

@Injectable()
export class DetailButtonService {
  currentState: AppState | null = null;
  product: SearchResultModel | null = null;

  constructor(
    private loggingService: LoggingService,
    private stateService: StateService
  ) { }

  activateDetailButton() {
    if (this.currentState === null) {
      return false;
    }
    this.loggingService.logToConsole(this.currentState);
    this.loggingService.logToConsole(this.stateService.getCurrentState());
    if (this.currentState !== this.stateService.getCurrentState()) {
      this.clearDetailButton();
      return false;
    }
    return true;
  }

  setDetailButton(product: SearchResultModel) {
    this.clearDetailButton();
    product.highlighted = true;
    this.currentState = this.stateService.getCurrentState();
    this.product = product;
  }

  getProductId(): string {
    return this.product.productId;
  }

  clearDetailButton() {
    this.currentState = null;
    if (this.product !== null) {
      this.product.highlighted = false;
      this.product = null;
    }
  }
}
