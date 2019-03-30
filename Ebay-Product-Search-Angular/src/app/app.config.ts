import { isDevMode } from '@angular/core';

export class AppConfig {
  onLocal = true;

  constructor() {
    this.onLocal = isDevMode();
  }

  getApiEndPoint() {
    if (this.onLocal) {
      return 'http://192.168.0.113:8081/api';
    } else {
      return 'http://ebay-product-search-angular.appspot.com/api';
    }
  }
}
