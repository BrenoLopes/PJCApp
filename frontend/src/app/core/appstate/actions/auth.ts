import {ActionTypes, PayloadedActions} from '@core/appstate/actions/index';
import {AuthenticationState} from '@core/appstate/models/auth.model';

export class SetAuthenticationAction implements PayloadedActions {
  readonly type: ActionTypes = ActionTypes.SET_AUTHENTICATION;

  constructor(public payload: AuthenticationState) {
    // Wraps the authentication data in the payload variable
    // and sets its type
  }
}
