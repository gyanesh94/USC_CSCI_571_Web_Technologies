import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { LoggingService } from './logging.service';

@Injectable()
export class GeoLocationService {
  private geoLocationeRecived = false;
  private zipcode: string | null;

  constructor(
    private http: HttpClient,
    private loggingService: LoggingService
  ) { }

  isGeoLocationReceived(): boolean {
    return this.geoLocationeRecived;
  }

  callGeoLocationApi() {
    this.http.get('http://ip-api.com/json')
      .subscribe(
        (response: {zip: string} | null) => {
          if (response === null || !response.hasOwnProperty('zip')) {
            this.geoLocationeRecived = false;
            this.zipcode = null;
            return;
          }
          this.geoLocationeRecived = true;
          this.zipcode = response.zip;
        },
        error => { }
      );
  }

  getCurrentZipCode() {
    return this.zipcode;
  }
}
