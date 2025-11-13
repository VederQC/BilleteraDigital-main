import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { EntityDataService } from '../../utils/entity-data';

export interface Wallet {
  id: number;
  userId: number;
  balance: number;
  currency: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class WalletService extends EntityDataService<Wallet> {
  constructor(protected override httpClient: HttpClient) {
    super(httpClient, 'wallets');
  }

  // Nuevo método para obtener la wallet por userId
  public getWalletByUserId$(userId: number): Observable<Wallet> {
    return this.httpClient.get<Wallet>(`${environment.apiUrl}/wallets/user/${userId}`);
  }
  public createWallet$(wallet: Partial<Wallet>): Observable<Wallet> {
    return this.httpClient.post<Wallet>('wallets', wallet);
  }
  

  // ✅ 4. Eliminar wallet por usuario
  public deleteWalletByUserId$(userId: number): Observable<void> {
    return this.httpClient.delete<void>(`wallets/user/${userId}`);
  }
}
