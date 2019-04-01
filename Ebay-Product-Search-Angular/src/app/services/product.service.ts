import { Injectable } from '@angular/core';
import { HttpParams, HttpClient, HttpErrorResponse } from '@angular/common/http';

import { StateService } from './state.service';
import { AppState } from '../models/appState.model';
import { AppConfig } from '../app.config';
import { LoggingService } from './logging.service';
import { ProductModel } from '../models/product.model';
import { ShippingModel } from '../models/shipping.model';
import { SearchResultModel } from '../models/searchResult.model';
import { SimilarProductModel } from '../models/similarProduct.model';

@Injectable()
export class ProductService {
  productId: string | null = null;
  gotProductData = false;
  productData: ProductModel | null;
  shippingData: ShippingModel | null;

  gotSimilarItemsData = false;
  similarProductData: SimilarProductModel[];
  similarProductError: string | null;

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
    const params = new HttpParams().set('productId', this.productId);
    const apiEndPoint = this.appConfig.getApiEndPoint();
    const url = `${apiEndPoint}/similarProduct`;

    this.http.get(url, { params })
      .subscribe(
        (response: SimilarProductModel[]) => {
          this.similarProductError = null;

          this.similarProductData = response;
          this.gotSimilarItemsData = true;

          this.moveToProductPage();
        },
        (error: HttpErrorResponse) => {
          this.similarProductError = error.error;
          this.gotSimilarItemsData = true;

          this.moveToProductPage();
        }
      );
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
    this.gotProductData = false;
    this.productId = null;
    this.productData = null;
    this.shippingData = null;
    this.errorMessage = '';

    this.gotSimilarItemsData = false;
    this.similarProductError = null;
    this.similarProductData = null;

    this.gotGoogleCustomEngineImages = false;
  }
}
