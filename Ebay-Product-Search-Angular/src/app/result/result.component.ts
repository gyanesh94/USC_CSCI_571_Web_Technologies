import { Component, OnInit } from '@angular/core';
import { PageChangedEvent } from 'ngx-bootstrap/pagination/public_api';

import { SearchResultService } from '../services/searchResult.service';
import { LoggingService } from '../services/logging.service';
import { SearchResultModel } from '../models/searchResult.model';
import { WishListService } from '../services/wishList.service';
import { DetailButtonService } from '../services/detailButton.service';
import { ProductService } from '../services/product.service';
import { AppState } from '../models/appState.model';

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
  disableDetailButton = true;
  highLightedProductId: string | null = null;

  constructor(
    private loggingService: LoggingService,
    private searchResultService: SearchResultService,
    private wishListService: WishListService,
    private detailButtonService: DetailButtonService,
    private productService: ProductService
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
    if (this.detailButtonService.activateDetailButton(AppState.ResultComponent)) {
      this.disableDetailButton = false;
      this.highLightedProductId = this.detailButtonService.getProductResultData(AppState.ResultComponent).productId;
    }
  }

  pageChanged(event: PageChangedEvent) {
    const startItem = (event.page - 1) * event.itemsPerPage;
    const endItem = event.page * event.itemsPerPage;
    this.returnedArray = this.searchResultData.slice(startItem, endItem);
  }

  showProductDetail(productId: string) {
    for (const product of this.returnedArray) {
      if (product.productId === productId) {
        this.detailButtonService.setDetailButton(product, AppState.ResultComponent);
        this.disableDetailButton = false;
        this.highLightedProductId = productId;
        this.productService.fetchData(product);
        break;
      }
    }
  }

  onImageError(i: number) {
    this.returnedArray[i].image = null;
  }

  addOrRemoveFromWishList(productId: string) {
    for (const product of this.searchResultData) {
      if (productId === product.productId) {
        if (product.inWishList) {
          this.wishListService.removeProductFromWishList(productId);
        } else {
          this.wishListService.addProductToWishList(product);
        }
        return;
      }
    }
  }

  detailButtonClicked() {
    this.productService.fetchData(this.detailButtonService.getProductResultData(AppState.ResultComponent));
  }
}
