import { Component, OnInit } from '@angular/core';

import { LoggingService } from '../services/logging.service';
import { WishListService } from '../services/wishList.service';
import { SearchResultModel } from '../models/searchResult.model';

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

  constructor(
    private loggingService: LoggingService,
    private wishListService: WishListService
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
    this.loggingService.logToConsole(productId);
  }

  removeFromWishList(productId: string) {
    this.wishListService.removeProductFromWishList(productId);
    if (!this.wishList.length) {
      this.haveError = true;
      this.errorMessage = 'No Records.';
    }
    this.calculateTotalPrice();
  }
}
