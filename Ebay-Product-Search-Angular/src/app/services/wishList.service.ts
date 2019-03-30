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
    localStorage.setItem('wishList', JSON.stringify(this.wishList));
  }

  getWishListFromLocalStorage() {
    const localData = localStorage.getItem('wishList');
    if (!localData && localData !== null) {
      for (const res of JSON.parse(localData)) {
        this.wishList.push(
          Object.assign(new SearchResultModel(), res)
        );
      }
    }
  }

  mapSearchResultToWishList(searchResult: SearchResultModel[]) {
    for (let i = 0; i < this.wishList.length; i++) {
      const wishListItemId = this.wishList[i].itemId;
      for (const result of searchResult) {
        if (wishListItemId === result.itemId) {
          result.inWishList = true;
          this.wishList[i] = result;
          break;
        }
      }
    }
  }
}
