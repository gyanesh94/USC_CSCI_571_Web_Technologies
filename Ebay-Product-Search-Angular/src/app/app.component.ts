import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { GeoLocationService } from './services/geoLocation.service';
import { LoggingService } from './services/logging.service';
import { StateService } from './services/state.service';
import { AppState } from './models/appState.model';
import { SearchResultService } from './services/searchResult.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [SearchResultService],
  animations: []
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Ebay-Product-Search-Angular';
  currentStatus = AppState.HomeComponent;
  appStatus = AppState;
  resultActive = true;
  subscriptions: Subscription[] = [];

  constructor(
    private geoLocationService: GeoLocationService,
    private loggingService: LoggingService,
    private stateService: StateService
  ) { }

  ngOnInit() {
    this.geoLocationService.callGeoLocationApi();
    this.subscriptions.push(
      this.stateService.stateChangeEvent.subscribe(
        (newState: AppState) => this.changeStatus(newState)
      )
    );
  }

  ngOnDestroy() {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  changeStatus(newState: AppState) {
    if (newState === AppState.HomeComponent || newState === AppState.ResultComponent) {
      this.resultActive = true;
    } else if (newState === AppState.WishListComponent) {
      this.resultActive = false;
    }
    this.loggingService.logToConsole(newState);
    this.currentStatus = newState;
    this.loggingService.logToConsole(this.currentStatus);
  }

}
