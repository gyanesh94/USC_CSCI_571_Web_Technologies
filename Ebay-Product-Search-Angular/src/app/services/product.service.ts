import { Injectable } from '@angular/core';
import { HttpParams, HttpClient, HttpErrorResponse } from '@angular/common/http';

import { StateService } from './state.service';
import { AppState } from '../models/appState.model';
import { AppConfig } from '../app.config';
import { LoggingService } from './logging.service';
import { ProductModel } from '../models/product.model';
import { ShippingModel } from '../models/shipping.model';
import { SearchResultModel } from '../models/searchResult.model';

@Injectable()
export class ProductService {
  productId: string | null = null;
  gotProductData = false;
  productData: ProductModel | null;
  shippingData: ShippingModel | null;

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

  fetchData(searchProduct: SearchResultModel) {
    this.loggingService.logToConsole(searchProduct.productId);
    this.stateService.updateState(AppState.ProgressBar);
    this.clearData();

    this.productId = searchProduct.productId;
    this.shippingData = searchProduct.shipping;
    this.fetchProductInformation();
    this.fetchImages();
    this.fetchSimilarItemsDetail();
  }

  fetchProductInformation() {
    const params = new HttpParams().set('productId', this.productId);
    const apiEndPoint = this.appConfig.getApiEndPoint();
    const url = `${apiEndPoint}/productInfo`;

    this.http.get(url, { params })
      .subscribe(
        (response: ProductModel) => {
          this.haveError = false;
          this.errorMessage = '';

          this.productData = response;
          this.gotProductData = true;

          this.moveToProductPage();
        },
        (error: HttpErrorResponse) => {
          this.haveError = true;
          this.errorMessage = error.error;
          this.gotProductData = true;
          this.moveToProductPage();
        }
      );
  }

  fetchImages() {

  }

  fetchSimilarItemsDetail() {

  }

  moveToProductPage() {
    if (
      this.gotProductData &&
      this.gotSimilarItemsData &&
      this.gotGoogleCustomEngineImages
    ) {
      this.stateService.updateState(AppState.ProductComponent);
      return;
    }
  }

  clearData() {
    this.productId = null;
    this.shippingData = null;
    this.gotProductData = false;
    this.gotSimilarItemsData = false;
    this.gotGoogleCustomEngineImages = false;
  }
}
