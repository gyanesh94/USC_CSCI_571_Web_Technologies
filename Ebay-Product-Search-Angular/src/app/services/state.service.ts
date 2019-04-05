import { EventEmitter } from '@angular/core';
import { AppState } from '../models/appState.model';

export class StateService {
  private previousState = AppState.HomeComponent;
  private currentState = AppState.HomeComponent;
  private nextState: AppState | null = null;
  private stateChangeEvent = new EventEmitter<string>();

  updateState(newState: AppState, nextState = null) {
    if (newState !== AppState.ProgressBar) {
      this.previousState = this.currentState;
      this.currentState = newState;
    }
    this.nextState = nextState;
    this.stateChangeEvent.emit(newState);
  }

  getPreviousState() {
    return this.previousState;
  }

  getCurrentState() {
    return this.currentState;
  }

  getNextState() {
    return this.nextState;
  }

  getEventsubscription() {
    return this.stateChangeEvent;
  }
}
