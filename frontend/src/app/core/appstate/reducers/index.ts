import {Action} from '@ngrx/store';

import {ApplicationState} from '@core/appstate';
import {AuthenticationState} from '@core/appstate/models/auth.model';
import {ActionTypes, PayloadedActions} from '@core/appstate/actions';

const initialAuthState: AuthenticationState = {
  isInitialState: true,
  isAuth: false,
  username: '',
};

const initialState: ApplicationState = {
  authContext: initialAuthState,
};

export function reducer(state: ApplicationState = initialState, action: Action): ApplicationState {
  switch (action.type) {
    case ActionTypes.SET_AUTHENTICATION:
      return {
        ...state,
        authContext: (action as PayloadedActions).payload,
      };
    case ActionTypes.DEFAULT:
    default:
      return state;
  }
}
