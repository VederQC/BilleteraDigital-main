import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { EntityDataService } from '../../utils/entity-data';
import { Category } from '../../models/category.model';
import { Subcategory } from '../../models/subcategory.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends EntityDataService<Category> {

  constructor(protected override httpClient: HttpClient) {
    // 👉 "categories" será el endpoint base
    // Todas las operaciones del CRUD genérico usarán:  /categories
    super(httpClient, 'categories');
  }

  // ===========================================================
  // 🔹 CATEGORÍAS
  // ===========================================================

  /** Obtener todas las categorías por usuario */
  public getCategoriesByUser$(userId: number): Observable<Category[]> {
    return this.httpClient.get<Category[]>(`categories/user/${userId}`);
  }

  /** Crear categoría */
  public createCategory$(payload: Partial<Category>): Observable<Category> {
    return this.httpClient.post<Category>(`categories`, payload);
  }

  /** Actualizar categoría */
  public updateCategory$(id: number, payload: Partial<Category>): Observable<Category> {
    return this.httpClient.put<Category>(`categories/${id}`, payload);
  }

  /** Eliminar categoría por ID */
  public deleteCategory$(id: number): Observable<void> {
    return this.httpClient.delete<void>(`categories/${id}`);
  }

  // ===========================================================
  // 🔹 SUBCATEGORÍAS
  // ===========================================================

  /** Obtener subcategorías de una categoría */
  public getSubcategories$(categoryId: number): Observable<Subcategory[]> {
    return this.httpClient.get<Subcategory[]>(`categories/${categoryId}/subcategories`);
  }

  /** Crear subcategoría dentro de una categoría */
  public createSubcategory$(categoryId: number, payload: Partial<Subcategory>): Observable<Subcategory> {
    return this.httpClient.post<Subcategory>(`categories/${categoryId}/subcategories`, payload);
  }

  /** Actualizar subcategoría */
  public updateSubcategory$(categoryId: number, subId: number, payload: Partial<Subcategory>): Observable<Subcategory> {
    return this.httpClient.put<Subcategory>(`categories/${categoryId}/subcategories/${subId}`, payload);
  }

  /** Eliminar subcategoría */
  public deleteSubcategory$(categoryId: number, subId: number): Observable<void> {
    return this.httpClient.delete<void>(`categories/${categoryId}/subcategories/${subId}`);
  }
}
