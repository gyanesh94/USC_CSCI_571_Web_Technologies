import { Component, OnInit } from '@angular/core';

import { LoggingService } from '../services/logging.service';
import { WishListService } from '../services/wishList.service';
import { SearchResultModel } from '../models/searchResult.model';
import { DetailButtonService } from '../services/detailButton.service';
import { ProductService } from '../services/product.service';

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

  constructor(
    private loggingService: LoggingService,
    private wishListService: WishListService,
    private detailButtonService: DetailButtonService,
    private productService: ProductService
  ) { }

  ngOnInit() {
    this.wishList = this.wishListService.wishList;
    if (!this.wishList.length) {
      this.haveError = true;
      this.errorMessage = 'No Records.';
    } else {
      this.haveError = false;
      this.errorMessage = '';
      this.calculateTotalPrice();
    }
    if (this.detailButtonService.activateDetailButton()) {
      this.disableDetailButton = false;
    }
  }

  calculateTotalPrice() {
    this.totalCost = 0;
    for (const product of this.wishList) {
      if (product.price && product.price !== null) {
        this.totalCost += Number(product.price.substring(1));
      }
    }
  }

  showProductDetail(productId: string) {
    for (const product of this.wishList) {
      if (product.productId === productId) {
        this.detailButtonService.setDetailButton(product);
        this.disableDetailButton = false;
        this.productService.fetchData(product);
      }
    }
  }

  removeFromWishList(productId: string) {
    if (this.detailButtonService.getProductResultData().productId === productId) {
      this.detailButtonService.clearDetailButton();
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
    this.productService.fetchData(this.detailButtonService.getProductResultData());
  }
}
