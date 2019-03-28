import { Component, OnInit } from '@angular/core';

import { GeoLocationService } from './services/geoLocation.service';
import { LoggingService } from './services/logging.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [GeoLocationService]
})
export class AppComponent implements OnInit {
  title = 'Ebay-Product-Search-Angular';

  constructor(
    private geoLocationService: GeoLocationService,
    private loggingService: LoggingService
  ) { }

  ngOnInit() {
    this.geoLocationService.callGeoLocationApi();
  }

}
