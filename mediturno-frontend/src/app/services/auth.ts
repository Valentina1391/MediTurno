import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class Auth {
  private api = 'https://localhost:8443/api/auth';

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    return this.http.post<any>(`${this.api}/login`, { email, password }).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('rol', res.rol);
        localStorage.setItem('nombre', res.nombre);
        localStorage.setItem('id', String(res.id));
      })
    );
  }

  loginGoogle(credential: string) {
    return this.http.post<any>(`${this.api}/google`, { credential }).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('rol', res.rol);
        localStorage.setItem('nombre', res.nombre);
        localStorage.setItem('id', String(res.id));
      })
    );
  }

  registro(nombre: string, email: string, password: string) {
    return this.http.post<any>(`${this.api}/registro`, { nombre, email, password });
  }

  logout() { localStorage.clear(); }
  get token() { return localStorage.getItem('token'); }
  get rol() { return localStorage.getItem('rol'); }
  get nombre() { return localStorage.getItem('nombre'); }
  get id() { return Number(localStorage.getItem('id')); }
  get logueado() { return !!this.token; }
}
