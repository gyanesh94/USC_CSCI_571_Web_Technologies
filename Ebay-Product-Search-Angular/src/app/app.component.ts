import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import {
  trigger,
  state,
  style,
  transition,
  animate,
} from '@angular/animations';

import { GeoLocationService } from './services/geoLocation.service';
import { LoggingService } from './services/logging.service';
import { StateService } from './services/state.service';
import { AppState } from './models/appState.model';
import { SearchResultService } from './services/searchResult.service';
import { WishListService } from './services/wishList.service';
import { DetailButtonService } from './services/detailButton.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [SearchResultService, WishListService, DetailButtonService],
  animations: [
    trigger('productState', [
      transition('void => *', [
        style({
          transform: 'translateX(-100%)'
        }),
        animate(700)
      ]),
      transition('* => out', [
        animate(1000, style({
          transform: 'translateX(100%)'
        }))
      ])
    ]),
    trigger('otherStates', [
      state('in', style({
        transform: 'translateX(0)'
      })),
      transition('void => in', [
        style({
          transform: 'translateX(-100%)'
        }),
        animate(700)
      ])
    ])
  ]
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Ebay-Product-Search-Angular';
  currentState = AppState.HomeComponent;
  appState = AppState;
  resultActive = true;
  subscriptions: Subscription[] = [];

  resultWishState = 'out';
  progressBarState = 'out';

  constructor(
    private geoLocationService: GeoLocationService,
    private loggingService: LoggingService,
    private stateService: StateService,
    private wishListService: WishListService
  ) { }

  ngOnInit() {
    this.geoLocationService.callGeoLocationApi();
    this.subscriptions.push(
      this.stateService.getEventsubscription().subscribe(
        (newState: AppState) => {
          if (newState === AppState.HomeComponent || newState === AppState.ResultComponent) {
            this.resultActive = true;
          } else if (newState === AppState.WishListComponent) {
            this.resultActive = false;
          }
          this.animationWorking(newState);
        }
      )
    );
    this.wishListService.getWishListFromLocalStorage();
  }

  animationWorking(newState: AppState) {
    if (this.stateService.getPreviousState() === AppState.ProductComponent) {
      this.resultWishState = 'in';
    } else {
      this.resultWishState = 'out';
    }
    if (this.stateService.getNextState() === AppState.ProductComponent) {
      this.progressBarState = 'in';
    } else {
      this.progressBarState = 'out';
    }
    this.currentState = newState;
  }

  ngOnDestroy() {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  changeState(newState: AppState) {
    this.stateService.updateState(newState);
  }
}
