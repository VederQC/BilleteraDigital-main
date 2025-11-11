import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export class EntityDataService<T> {

  constructor(
    protected httpClient: HttpClient,
    protected endPoint: string,
  ) {}

  // GET ALL - Obtener todos los registros
  public getAll$(): Observable<T> {
    return this.httpClient.get<T>(`${this.endPoint}`);
  }

  // GET BY ID - Obtener un registro por ID
  public getById$(id: string): Observable<T> {
    return this.httpClient.get<T>(`${this.endPoint}/${id}`);
  }

  // POST - Crear un nuevo registro
  public add$(entity: any): Observable<T> {
    return this.httpClient.post<T>(`${this.endPoint}`, entity);
  }

  // PUT - Actualizar un registro existente
  public update$(id: string, entity: any): Observable<T> {
    return this.httpClient.put<T>(`${this.endPoint}/${id}`, entity);
  }

  // DELETE - Eliminar un registro
  public delete$(id: string): Observable<any> {
    return this.httpClient.delete<any>(`${this.endPoint}/${id}`);
  }
}
