import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CategoryDTO {
  id: number;
  name: string;
  icon?: string;
  color?: string;
}

export interface SubcategoryDTO {
  id: number;
  name: string;
  icon?: string;
  color?: string;
}

export interface EventDTO {
  id: number;
  name: string;
  description?: string;
}

export type TransactionType = 'INCOME' | 'EXPENSE';

export interface TransactionRequest {
  userId: number;
  categoryId: number;
  subcategoryId: number;
  eventId?: number | null;
  type: TransactionType;
  amount: number;
  description: string;
}

export interface TransactionResponse {
  id: number;
  walletId: number;
  userId: number;
  category: CategoryDTO;
  subcategory: SubcategoryDTO;
  event?: EventDTO | null;
  type: TransactionType;
  amount: number;
  description: string;
  transactionDate: string;
}

@Injectable({ providedIn: 'root' })
export class TransactionService {
  constructor(private http: HttpClient) {}

  createTransaction$(payload: TransactionRequest): Observable<TransactionResponse> {
    // urlInterceptor le agrega environment.apiUrl
    return this.http.post<TransactionResponse>('transactions', payload);
  }

  getUserTransactions$(
    userId: number,
    startDate?: string,
    endDate?: string
  ): Observable<TransactionResponse[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<TransactionResponse[]>(`transactions/user/${userId}`, { params });
  }
}
