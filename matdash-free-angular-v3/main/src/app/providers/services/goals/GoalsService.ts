import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GoalsService {

  constructor(private http: HttpClient) {}

  // 📌 Crear meta
  createGoal$(payload: any): Observable<any> {
    return this.http.post('goals', payload);
  }

  // 📌 Obtener metas por usuario
  getGoalsByUser$(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`goals/user/${userId}`);
  }

  // 📌 Editar meta COMPLETA
  updateGoal$(goalId: number, payload: any): Observable<any> {
    return this.http.put(`goals/${goalId}`, payload);
  }

  // 📌 Eliminar meta
  deleteGoal$(goalId: number): Observable<any> {
    return this.http.delete(`goals/${goalId}`);
  }

  // 📌 Aportar monto a una meta
  updateGoalAmount$(goalId: number, amount: number): Observable<any> {
    return this.http.put(`goals/${goalId}/amount`, null, {
      params: { amountChange: amount }
    });
  }

}
