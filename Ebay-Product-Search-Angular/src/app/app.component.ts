import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { GeoLocationService } from './services/geoLocation.service';
import { LoggingService } from './services/logging.service';
import { StateService } from './services/state.service';
import { AppState } from './models/appState.model';
import { SearchResultService } from './services/searchResult.service';
import { WishListService } from './services/wishList.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [SearchResultService, WishListService],
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
    private stateService: StateService,
    private wishListService: WishListService
  ) { }

  ngOnInit() {
    this.geoLocationService.callGeoLocationApi();
    this.subscriptions.push(
      this.stateService.stateChangeEvent.subscribe(
        (newState: AppState) => this.changeStatus(newState)
      )
    );
    this.wishListService.getWishListFromLocalStorage();
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
    this.currentStatus = newState;
  }

}
