import {AuthenticationState} from '@core/appstate/models/auth.model';

export interface ApplicationState {
  readonly authContext: AuthenticationState;
}
