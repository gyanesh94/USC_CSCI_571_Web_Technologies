import { isDevMode } from '@angular/core';

export class AppConfig {
  onLocal = true;

  constructor() {
    this.onLocal = isDevMode();
  }

  getApiEndPoint() {
    if (this.onLocal) {
      return 'http://localhost:8081/api';
    } else {
      return 'http://ebay-product-search-angular.appspot.com/api';
    }
  }
}
