import { Injectable } from '@angular/core';

import { SearchResultModel } from '../models/searchResult.model';
import { LoggingService } from './logging.service';

@Injectable()
export class WishListService {
  wishList: SearchResultModel[] = [];

  constructor(
    private loggingService: LoggingService
  ) { }

  setWishListToLocalStorage() {
    localStorage.clear();
    localStorage.setItem('wishList', JSON.stringify(this.wishList));
  }

  getWishListFromLocalStorage() {
    const localData = localStorage.getItem('wishList');
    if (localData && localData !== null) {
      this.wishList = JSON.parse(localData);
    }
  }

  mapSearchResultToWishList(searchResult: SearchResultModel[]) {
    for (let i = 0; i < this.wishList.length; i++) {
      const wishListproductId = this.wishList[i].productId;
      for (const result of searchResult) {
        if (wishListproductId === result.productId) {
          result.inWishList = true;
          this.wishList[i] = result;
          break;
        }
      }
    }
  }

  removeProductFromWishList(productId: string) {
    for (let i = 0; i < this.wishList.length; i++) {
      if (this.wishList[i].productId === productId) {
        this.wishList[i].inWishList = false;
        this.wishList.splice(i, 1);
        this.setWishListToLocalStorage();
        return;
      }
    }
  }

  addProductToWishList(product: SearchResultModel) {
    product.inWishList = true;
    this.wishList.push(product);
    this.setWishListToLocalStorage();
  }
}
