import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subscription } from 'rxjs';

import { ProductService } from 'src/app/services/product.service';
import { SimilarProductModel } from 'src/app/models/similarProduct.model';
import { LoggingService } from 'src/app/services/logging.service';

@Component({
  selector: 'app-similar-products',
  templateUrl: './similar-products.component.html',
  styleUrls: ['./similar-products.component.css']
})
export class SimilarProductsComponent implements OnInit, OnDestroy {
  haveError = false;
  errorMessage = '';

  similarProductData: SimilarProductModel[];
  resultData: SimilarProductModel[];

  sortByFormControl: FormControl;
  orderByFormControl: FormControl;

  subscriptions: Subscription[] = [];
  disableValueChangeSubscription = false;
  firstFiveShown = true;

  constructor(
    private loggingService: LoggingService,
    private productService: ProductService
  ) { }

  ngOnInit() {
    if (this.productService.getSimilarProductErrorMessage() !== null) {
      this.haveError = true;
      this.errorMessage = this.productService.getSimilarProductErrorMessage();
    } else {
      this.similarProductData = this.productService.getSimilarProductData();
      this.sortByFormControl = new FormControl('default');
      this.orderByFormControl = new FormControl({ value: -1, disabled: true });

      this.subscriptions.push(
        this.sortByFormControl.valueChanges.subscribe(_ => {
          if (!this.disableValueChangeSubscription) {
            this.updateTheResults();
          }
        })
      );

      this.subscriptions.push(
        this.orderByFormControl.valueChanges.subscribe(_ => {
          if (!this.disableValueChangeSubscription) {
            this.updateTheResults();
          }
        })
      );

      if (this.similarProductData.length <= 5) {
        this.resultData = this.similarProductData;
      } else {
        this.updateResultData();
      }
    }
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  showFirstFive() {
    this.resultData = [];
    for (let i = 0; i < 5; i++) {
      this.resultData.push(this.similarProductData[i]);
    }
  }

  updateResultData() {
    if (this.firstFiveShown) {
      this.showFirstFive();
    } else {
      this.resultData = this.similarProductData;
    }
  }

  showMoreOrLess() {
    this.firstFiveShown = !this.firstFiveShown;
    this.updateResultData();
  }

  updateTheResults() {
    this.disableValueChangeSubscription = true;
    const sortByValue: string = this.sortByFormControl.value;
    if (sortByValue === 'default') {
      this.orderByFormControl.disable();
      this.similarProductData = this.productService.getSimilarProductData();
      this.updateResultData();
      return;
    } else {
      this.orderByFormControl.enable();
    }

    const returnValue = this.orderByFormControl.value;

    this.similarProductData.sort((a: SimilarProductModel, b: SimilarProductModel) => {
      if (a[sortByValue] < b[sortByValue]) {
        return returnValue;
      } else {
        return -1 * returnValue;
      }
    });

    this.updateResultData();
    this.disableValueChangeSubscription = false;
  }
}
