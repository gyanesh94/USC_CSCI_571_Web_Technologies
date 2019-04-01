import { Component, OnInit, TemplateRef } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

import { LoggingService } from 'src/app/services/logging.service';
import { ProductService } from 'src/app/services/product.service';
import { ProductModel } from 'src/app/models/product.model';

@Component({
  selector: 'app-information',
  templateUrl: './information.component.html',
  styleUrls: ['./information.component.css']
})
export class InformationComponent implements OnInit {
  productData: ProductModel;
  modalRef: BsModalRef;

  constructor(
    private loggingService: LoggingService,
    private productService: ProductService,
    private modalService: BsModalService
  ) { }

  ngOnInit() {
    this.productData = this.productService.getProductData();
    this.loggingService.logToConsole(this.productData);
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }
}
