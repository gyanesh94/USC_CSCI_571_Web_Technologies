import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';

import { LoggingService } from './logging.service';
import { AppConfig } from '../app.config';

@Injectable()
export class ZipcodeSuggestionsService {

  constructor(
    private http: HttpClient,
    private loggingService: LoggingService,
    private appConfig: AppConfig
  ) { }

  callGeoLocationApi(search: string) {
    const params = new HttpParams().set('zipcode', search);
    const apiEndPoint = this.appConfig.getApiEndPoint();
    const url = `${apiEndPoint}/zipcode`;
    return this.http.get(url, { params })
      .pipe(
        map(
          (response: { postalCodes: { postalCode: string }[] }) => {
            const result: string[] = [];
            if (response.hasOwnProperty('postalCodes')) {
              for (const postalCode of response.postalCodes) {
                if (postalCode.hasOwnProperty('postalCode')) {
                  result.push(postalCode.postalCode);
                }
              }
            }
            return result;
          },
          _ => []
        )
      );
  }
}
