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
  private productId: string | null = null;
  private gotProductData = false;
  private productData: ProductModel | null = null;
  private shippingData: ShippingModel | null = null;

  private gotSimilarItemsData = false;
  private similarProductData: SimilarProductModel[] = null;
  private similarProductError: string | null = null;

  private gotGoogleCustomEngineImages = false;
  private productTitle: string | null = null;
  private googleProductImages: string[] = null;
  private googleProductImagesError: string | null = null;

  private errorMessage = '';
  private haveError = false;

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
    this.productTitle = searchProduct.title;
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
    const params = new HttpParams().set('query', this.productTitle);
    const apiEndPoint = this.appConfig.getApiEndPoint();
    const url = `${apiEndPoint}/googleImages`;

    this.http.get(url, { params })
      .subscribe(
        (response: string[]) => {
          this.googleProductImagesError = null;

          this.googleProductImages = response;
          this.gotGoogleCustomEngineImages = true;

          this.moveToProductPage();
        },
        (error: HttpErrorResponse) => {
          this.loggingService.logToConsole(error);
          this.googleProductImagesError = error.error;
          this.gotGoogleCustomEngineImages = true;

          this.moveToProductPage();
        }
      );
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
    this.productTitle = null;
    this.googleProductImages = null;
    this.googleProductImagesError = null;
  }
}
