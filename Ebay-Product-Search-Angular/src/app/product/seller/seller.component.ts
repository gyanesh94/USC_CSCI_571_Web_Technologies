import { Component, OnInit } from '@angular/core';

import { SellerModel } from 'src/app/models/seller.model';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-seller',
  templateUrl: './seller.component.html',
  styleUrls: ['./seller.component.css']
})
export class SellerComponent implements OnInit {
  haveError = false;
  errorMessage = '';
  sellerData: SellerModel;

  constructor(
    private productService: ProductService
  ) { }

  ngOnInit() {
    this.sellerData = this.productService.getSellerData();
    let flag = true;
    for (const key in this.sellerData) {
      if (this.sellerData[key] != null) {
        flag = false;
        break;
      }
    }
    if (flag) {
      this.haveError = true;
      this.errorMessage = 'No seller data found.';
    }
  }

}
