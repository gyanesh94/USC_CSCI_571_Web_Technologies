import { Component, OnInit } from '@angular/core';
import { PageChangedEvent } from 'ngx-bootstrap/pagination/public_api';

import { SearchResultService } from '../services/searchResult.service';
import { LoggingService } from '../services/logging.service';
import { SearchResultModel } from '../models/searchResult.model';
import { WishListService } from '../services/wishList.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {
  searchResultData: SearchResultModel[];
  haveError = true;
  errorMessage = 'No Records.';
  returnedArray: SearchResultModel[];

  constructor(
    private loggingService: LoggingService,
    private searchResultService: SearchResultService,
    private wishListService: WishListService
  ) { }

  ngOnInit() {
    if (this.searchResultService.gotErrors()) {
      this.haveError = true;
      this.errorMessage = this.searchResultService.getErrorMessage();
    } else {
      this.haveError = false;
      this.errorMessage = '';
      this.searchResultData = this.searchResultService.getData();
      this.wishListService.mapSearchResultToWishList(this.searchResultData);
      this.returnedArray = this.searchResultData.slice(0, 10);
    }
  }

  pageChanged(event: PageChangedEvent) {
    const startItem = (event.page - 1) * event.itemsPerPage;
    const endItem = event.page * event.itemsPerPage;
    this.returnedArray = this.searchResultData.slice(startItem, endItem);
  }

  showProductDetail(productId: string) {
    this.loggingService.logToConsole(productId);
  }

  onImageError(i: number) {
    this.returnedArray[i].image = null;
  }

  addOrRemoveFromWishList(productId: string) {
    for (const product of this.searchResultData) {
      if (productId === product.productId) {
        if (product.inWishList) {
          product.inWishList = false;
          this.wishListService.removeProductFromWishList(productId);
        } else {
          product.inWishList = true;
          this.wishListService.addProductToWishList(product);
        }
        return;
      }
    }
  }

}
