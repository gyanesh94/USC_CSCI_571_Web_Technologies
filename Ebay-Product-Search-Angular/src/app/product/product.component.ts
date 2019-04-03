import { Component, OnInit, OnDestroy } from '@angular/core';
import { MediaObserver, MediaChange } from '@angular/flex-layout';
import { Subscription, generate } from 'rxjs';

import { ProductModel } from '../models/product.model';
import { ProductTabState } from '../models/productTabState.model';
import { LoggingService } from '../services/logging.service';
import { ProductService } from '../services/product.service';
import { StateService } from '../services/state.service';
import { SearchResultModel } from '../models/searchResult.model';
import { WishListService } from '../services/wishList.service';
import { HttpParams } from '@angular/common/http';
import { AppConfig } from '../app.config';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit, OnDestroy {
  productData: ProductModel;
  searchResultData: SearchResultModel;

  productTabState = ProductTabState;
  activeTabState: ProductTabState = ProductTabState.ProductTab;

  similarProductTabString = '';
  mediaSubscription: Subscription;

  haveError = false;
  errorMessage = '';

  urlToFacebookShare: string;

  constructor(
    private loggingService: LoggingService,
    private productService: ProductService,
    private mediaObserver: MediaObserver,
    private stateService: StateService,
    private wishListService: WishListService,
    private appConfig: AppConfig
  ) { }

  ngOnInit() {
    this.mediaSubscription = this.mediaObserver.asObservable().subscribe((change: MediaChange[]) => {
      if (
        change[0].mqAlias === 'xs' ||
        change[0].mqAlias === 'sm' ||
        change[0].mqAlias === 'md'
      ) {
        this.similarProductTabString = 'Related';
      } else {
        this.similarProductTabString = 'Similar Products';
      }
    });

    this.haveError = this.productService.gotErrors();
    if (this.haveError) {
      this.errorMessage = this.productService.getErrorMessage();
    } else {
      this.productData = this.productService.getProductData();
      this.searchResultData = this.productService.getSearchResultData();
      this.generateShareDialogURL();
    }
  }

  ngOnDestroy() {
    this.mediaSubscription.unsubscribe();
  }

  switchTo(newTabState: ProductTabState) {
    this.activeTabState = newTabState;
  }

  goBackToList() {
    this.stateService.updateState(this.stateService.getPreviousState());
  }

  addOrRemoveFromWishList(productId: string) {
    if (this.searchResultData.inWishList) {
      this.wishListService.removeProductFromWishList(productId);
    } else {
      this.wishListService.addProductToWishList(this.searchResultData);
    }
  }

  generateShareDialogURL() {
    const url = 'https://www.facebook.com/dialog/share?';
    const params = new HttpParams()
      .set('app_id', this.appConfig.getFacebookAppId())
      .set('display', 'popup')
      .set('href', this.productData.productUrl)
      .set('quote', `Buy ${this.productData.title} at ${this.productData.price} from link below`)
      .set('redirect_uri', this.appConfig.getFacebookRedirectUri());

    this.urlToFacebookShare = url + params.toString();
  }
}
