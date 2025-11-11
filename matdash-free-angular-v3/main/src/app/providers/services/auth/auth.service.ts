import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { END_POINTS } from '../../utils/end-points';
import {EntityDataService} from "../../utils/entity-data";


@Injectable({ providedIn: 'root' })
export class AuthService extends EntityDataService<any[]> {

private readonly TOKEN_KEY   = 'auth_token' ;
  constructor(protected override httpClient: HttpClient) {
    super(httpClient, END_POINTS.login);
  }
  public getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token: string ): void {
    localStorage.clear();
    localStorage.setItem(this.TOKEN_KEY, token);
  }
}
