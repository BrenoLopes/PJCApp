import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '@env';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { Observable } from 'rxjs';
import {
  createMachine,
  interpret,
  Interpreter,
  MachineConfig,
  MachineOptions,
  StateMachine,
} from 'xstate';

@Injectable()
export class RefreshService {
  private serverUrl = environment.serverUrl;
  private machine!: Interpreter<{}>;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  public refreshJwtTokenIfExpired(
    onSuccess: VoidFunction = () => {},
    onForbidden: VoidFunction = () => {}
  ): void {
    if (this.localStorage.getJwtToken() === '') {
      onForbidden();
      return;
    }

    this.machine = interpret(
      this.buildStateMachine(
        this.sendApiRequest,
        this.sendRefreshRequest,
        onSuccess,
        onForbidden
      )
    );

    this.machine.start();
  }

  private sendApiRequest = (): void => {
    const success = (): void => {
      this.machine.send('LOADER_SUCCESS');
    };

    const failed = (): void => {
      this.machine.send('LOADER_FAILED');
    };

    this.createApiRequest().subscribe(success, failed);
  }

  private createApiRequest = (): Observable<{}> => {
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    return this.http.get(`${this.serverUrl}/api`, { headers });
  }

  private sendRefreshRequest = (): void => {
    const success = (response: RefreshResponse): void => {
      this.localStorage.setUsername(response.username);
      this.localStorage.setJwtToken(response.token);

      this.machine.send('REFRESH_SUCCESS');
    };

    const failed = (): void => {
      this.localStorage.removeUsername();
      this.localStorage.removeJwtToken();

      this.machine.send('REFRESH_FAILED');
    };

    this.createRefreshRequest().subscribe(success, failed);
  }

  private createRefreshRequest = (): Observable<RefreshResponse> => {
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    return this.http.get<RefreshResponse>(
      `${this.serverUrl}/api/auth/refresh`,
      { headers }
    );
  }

  private buildStateMachine(
    loadApi: VoidFunction,
    loadRefresh: VoidFunction,
    success: VoidFunction,
    forbidden: VoidFunction
  ): StateMachine<any, any, any> {
    const config: MachineConfig<any, any, any> = {
      id: 'theLoader',
      initial: 'loader',
      states: {
        loader: {
          entry: ['loadApi'],
          on: {
            LOADER_SUCCESS: { target: 'success' },
            LOADER_FAILED: { target: 'refresh' },
          },
        },
        refresh: {
          entry: ['loadRefresh'],
          on: {
            REFRESH_SUCCESS: { target: 'success' },
            REFRESH_FAILED: { target: 'redirect' },
          },
        },
        success: { entry: ['success'], type: 'final' },
        redirect: { entry: ['forbidden'], type: 'final' },
      },
    };

    const options: Partial<MachineOptions<any, any>> = {
      actions: {
        loadApi: () => loadApi(),
        loadRefresh: () => loadRefresh(),
        success: () => success(),
        forbidden: () => forbidden(),
      },
    };

    return createMachine(config, options);
  }
}

interface RefreshResponse {
  readonly username: string;
  readonly token: string;
}
