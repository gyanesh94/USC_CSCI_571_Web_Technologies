import { Component, OnInit } from '@angular/core';

import { LoggingService } from 'src/app/services/logging.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-photos',
  templateUrl: './photos.component.html',
  styleUrls: ['./photos.component.css']
})
export class PhotosComponent implements OnInit {
  haveError = false;
  errorMessage = '';

  googleImages: string[] = [];

  constructor(
    private loggingService: LoggingService,
    private productService: ProductService,
  ) { }

  ngOnInit() {
    if (this.productService.getErrorGooglePhotosErrorMessage() !== null) {
      this.haveError = true;
      this.errorMessage = this.productService.getErrorGooglePhotosErrorMessage();
    } else {
      this.googleImages = this.productService.getGoogleImages();
    }
  }

}
