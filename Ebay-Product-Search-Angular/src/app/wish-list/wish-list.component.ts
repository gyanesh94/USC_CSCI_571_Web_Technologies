import { Component, OnInit } from '@angular/core';

import { LoggingService } from '../services/logging.service';
import { WishListService } from '../services/wishList.service';
import { SearchResultModel } from '../models/searchResult.model';
import { DetailButtonService } from '../services/detailButton.service';
import { ProductService } from '../services/product.service';
import { AppState } from '../models/appState.model';

@Component({
  selector: 'app-wish-list',
  templateUrl: './wish-list.component.html',
  styleUrls: ['./wish-list.component.css']
})
export class WishListComponent implements OnInit {
  wishList: SearchResultModel[];
  haveError: boolean;
  errorMessage: string;
  totalCost: number;
  disableDetailButton = true;
  highLightedProductId: string | null = null;

  constructor(
    private loggingService: LoggingService,
    private wishListService: WishListService,
    private detailButtonService: DetailButtonService,
    private productService: ProductService
  ) { }

  ngOnInit() {
    this.wishList = this.wishListService.getWishList();
    if (!this.wishList.length) {
      this.haveError = true;
      this.errorMessage = 'No Records.';
    } else {
      this.haveError = false;
      this.errorMessage = '';
      this.calculateTotalPrice();
    }

    if (this.detailButtonService.activateDetailButton(AppState.WishListComponent)) {
      const detailProduct = this.detailButtonService.getProductResultData(AppState.WishListComponent);
      let flag = true;
      for (const product of this.wishList) {
        if (product.productId === detailProduct.productId) {
          this.disableDetailButton = false;
          this.highLightedProductId = product.productId;
          flag = false;
        }
      }
      if (flag) {
        this.detailButtonService.clearDetailButton(AppState.WishListComponent);
        this.disableDetailButton = true;
        this.highLightedProductId = null;
      }
    }
  }

  calculateTotalPrice() {
    this.totalCost = 0;
    for (const product of this.wishList) {
      if (product.price && product.price !== null) {
        this.totalCost += product.price;
      }
    }
  }

  showProductDetail(productId: string) {
    for (const product of this.wishList) {
      if (product.productId === productId) {
        this.detailButtonService.setDetailButton(product, AppState.WishListComponent);
        this.disableDetailButton = false;
        this.highLightedProductId = productId;

        this.productService.fetchData(product);
        break;
      }
    }
  }

  removeFromWishList(productId: string) {
    const detailProduct = this.detailButtonService.getProductResultData(AppState.WishListComponent);
    if (detailProduct !== null && detailProduct.productId === productId) {
      this.detailButtonService.clearDetailButton(AppState.WishListComponent);
      this.disableDetailButton = true;
    }
    this.wishListService.removeProductFromWishList(productId);
    if (!this.wishList.length) {
      this.haveError = true;
      this.errorMessage = 'No Records.';
    }
    this.calculateTotalPrice();
  }

  detailButtonClicked() {
    this.productService.fetchData(this.detailButtonService.getProductResultData(AppState.WishListComponent));
  }
}
