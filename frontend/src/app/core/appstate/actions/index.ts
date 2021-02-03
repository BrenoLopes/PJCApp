import { Action } from '@ngrx/store';
import { AuthenticationState } from '@core/appstate/models/auth.model';

export enum ActionTypes {
  DEFAULT= '@Actions/Default',
  SET_AUTHENTICATION = '@Actions/Authentication/Set',
}

export class PayloadedActions implements Action {
  readonly type: ActionTypes = ActionTypes.DEFAULT;

  constructor(public payload: AuthenticationState) {
    // Wraps the authentication data in the payload variable
    // and sets its type
  }
}
