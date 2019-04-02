import { Component, OnInit } from '@angular/core';

import { ProductService } from 'src/app/services/product.service';
import { ShippingModel } from 'src/app/models/shipping.model';

@Component({
  selector: 'app-shipping',
  templateUrl: './shipping.component.html',
  styleUrls: ['./shipping.component.css']
})
export class ShippingComponent implements OnInit {
  haveError = false;
  errorMessage = '';
  shippingData: ShippingModel;

  constructor(
    private productService: ProductService
  ) { }

  ngOnInit() {
    this.shippingData = this.productService.getShippingData();
    let flag = true;
    for (const key in this.shippingData) {
      if (this.shippingData[key] != null) {
        flag = false;
        break;
      }
    }
    if (flag) {
      this.haveError = true;
      this.errorMessage = 'No shipping data found.';
    }
  }

}
