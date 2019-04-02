import { EventEmitter } from '@angular/core';
import { AppState } from '../models/appState.model';

export class StateService {
  private previousState = AppState.HomeComponent;
  private currentState = AppState.HomeComponent;
  private stateChangeEvent = new EventEmitter<string>();

  updateState(newState: AppState) {
    if (newState !== AppState.ProgressBar) {
      this.previousState = this.currentState;
      this.currentState = newState;
    }
    this.stateChangeEvent.emit(newState);
  }

  getPreviousState() {
    return this.previousState;
  }

  getCurrentState() {
    return this.currentState;
  }

  getEventsubscription() {
    return this.stateChangeEvent;
  }
}
