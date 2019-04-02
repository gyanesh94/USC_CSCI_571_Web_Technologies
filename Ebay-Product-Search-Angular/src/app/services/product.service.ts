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
  private searchResultData: SearchResultModel;

  private gotProductData = false;
  private productData: ProductModel | null = null;
  private shippingData: ShippingModel | null = null;

  private gotSimilarItemsData = false;
  private similarProductData: SimilarProductModel[] = null;
  private similarProductError: string | null = null;

  private gotGoogleCustomEngineImages = false;
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

  fetchData(searchResultData: SearchResultModel) {
    this.stateService.updateState(AppState.ProgressBar);
    this.clearData();

    this.searchResultData = searchResultData;
    this.shippingData = searchResultData.shipping;
    this.fetchProductInformation();
    this.fetchImages();
    this.fetchSimilarItemsDetail();
  }

  fetchProductInformation() {
    const params = new HttpParams().set('productId', this.searchResultData.productId);
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
          if (typeof error.error === 'string') {
            this.errorMessage = error.error;
          } else {
            this.errorMessage = 'Server not working.';
          }
          this.gotProductData = true;
          this.moveToProductPage();
        }
      );
  }

  fetchImages() {
    this.gotGoogleCustomEngineImages = true;
    this.moveToProductPage();
    return;
    const params = new HttpParams().set('query', this.searchResultData.title);
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
          if (typeof error.error === 'string') {
            this.googleProductImagesError = error.error;
          } else {
            this.googleProductImagesError = 'Server not working.';
          }
          this.gotGoogleCustomEngineImages = true;

          this.moveToProductPage();
        }
      );
  }

  fetchSimilarItemsDetail() {
    const params = new HttpParams().set('productId', this.searchResultData.productId);
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
          if (typeof error.error === 'string') {
            this.similarProductError = error.error;
          } else {
            this.similarProductError = 'Server not working.';
          }
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

  getProductData() {
    return this.productData;
  }

  gotErrors() {
    return this.haveError;
  }

  getErrorMessage() {
    return this.errorMessage;
  }

  getSearchResultData() {
    return this.searchResultData;
  }

  getErrorGooglePhotosErrorMessage() {
    if (
      this.googleProductImages == null ||
      !this.googleProductImages.length
    ) {
      this.googleProductImagesError = 'No mages found.';
    }
    return this.googleProductImagesError;
  }

  getGoogleImages() {
    return this.googleProductImages;
  }

  getShippingData() {
    return this.shippingData;
  }


  clearData() {
    this.searchResultData = null;

    this.gotProductData = false;
    this.productData = null;
    this.shippingData = null;
    this.errorMessage = '';

    this.gotSimilarItemsData = false;
    this.similarProductError = null;
    this.similarProductData = null;

    this.gotGoogleCustomEngineImages = false;
    this.googleProductImages = null;
    this.googleProductImagesError = null;
  }
}
