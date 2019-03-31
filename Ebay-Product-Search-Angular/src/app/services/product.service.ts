import { Injectable } from '@angular/core';
import { HttpParams, HttpClient, HttpErrorResponse } from '@angular/common/http';

import { StateService } from './state.service';
import { AppState } from '../models/appState.model';
import { AppConfig } from '../app.config';
import { LoggingService } from './logging.service';
import { ProductModel } from '../models/product.model';

@Injectable()
export class ProductService {
  productId: string | null = null;
  gotProductData = false;
  productData: ProductModel;
  gotSimilarItemsData = false;
  gotGoogleCustomEngineImages = false;

  errorMessage = '';
  haveError = false;

  constructor(
    private loggingService: LoggingService,
    private appConfig: AppConfig,
    private stateService: StateService,
    private http: HttpClient
  ) { }

  fetchData(productId: string) {
    if (
      this.productId === productId &&
      this.gotProductData &&
      this.gotSimilarItemsData &&
      this.gotGoogleCustomEngineImages
    ) {
      this.stateService.updateState(AppState.ProductComponent);
      return;
    }
    this.stateService.updateState(AppState.ProgressBar);
    this.clearData();

    this.productId = productId;
    this.fetchProductInformation();
    this.fetchImages();
    this.fetchSimilarItemsDetail();
  }

  fetchProductInformation() {
    const params = new HttpParams().set('productId', this.productId);
    const apiEndPoint = this.appConfig.getApiEndPoint();
    const url = `${apiEndPoint}/productInfo`;
    this.loggingService.logToConsole(url + '?' + params.toString());
    this.http.get(url, { params })
      .subscribe(
        (response: {}) => {
          this.haveError = false;
          this.errorMessage = '';

          this.productData = Object.assign(new ProductModel(), response);
          this.gotProductData = true;
          this.loggingService.logToConsole(this.productData);
          this.stateService.updateState(AppState.ProductComponent);
        },
        (error: HttpErrorResponse) => {
          this.haveError = true;
          this.errorMessage = error.error;
          this.stateService.updateState(AppState.ProductComponent);
        }
      );
  }

  fetchImages() {

  }

  fetchSimilarItemsDetail() {

  }

  clearData() {
    this.productId = null;
    this.gotProductData = false;
    this.gotSimilarItemsData = false;
    this.gotGoogleCustomEngineImages = false;
  }
}
