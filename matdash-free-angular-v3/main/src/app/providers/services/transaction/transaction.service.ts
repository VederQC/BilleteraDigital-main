import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export enum TransactionType {
  INCOME = 'INCOME',
  EXPENSE = 'EXPENSE'
}


export interface TransactionRequest {
  userId: number;
  categoryId: number;
  subcategoryId: number;
  goalId?: number | null;
  eventId?: number | null;
  type: TransactionType;
  amount: number;
  description: string;
}

export interface TransactionResponse {
  id: number;
  walletId: number;
  userId: number;
  category: any;
  subcategory: any;
  event?: any;
  type: TransactionType;
  amount: number;
  description: string;
  transactionDate: string;
}

@Injectable({ providedIn: 'root' })
export class TransactionService {

  constructor(private http: HttpClient) {}

  createTransaction$(payload: TransactionRequest): Observable<TransactionResponse> {
    return this.http.post<TransactionResponse>('transactions', payload);
  }

  getUserTransactions$(userId: number, startDate?: string, endDate?: string): Observable<TransactionResponse[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<TransactionResponse[]>(`transactions/user/${userId}`, { params });
  }
}
