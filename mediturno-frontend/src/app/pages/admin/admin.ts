import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { Idioma } from '../../services/idioma';
import { SelectorIdioma } from '../../components/selector-idioma/selector-idioma';

@Component({
  selector: 'app-admin',
  imports: [FormsModule, SelectorIdioma],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin implements OnInit {
  private api = 'https://localhost:8443/api';
  medicos: any[] = [];
  especialidades: any[] = [];
  nombreMedico = '';
  especialidadId: number | null = null;
  nombreEspecialidad = '';
  error = '';
  resumen: any[] = [];

  constructor(private http: HttpClient, public auth: Auth, private router: Router, public i: Idioma) {}

  ngOnInit() {
    this.cargar();
    this.cargarResumen();
  }

  cargar() {
    this.http.get<any[]>(`${this.api}/medicos`).subscribe(m => (this.medicos = m));
    this.http.get<any[]>(`${this.api}/especialidades`).subscribe(e => (this.especialidades = e));
  }

  cargarResumen() {
    this.http.get<any[]>(`${this.api}/reportes/citas-por-estado`)
    .subscribe(r => (this.resumen = r));
  }

  crearEspecialidad() {
    this.error = '';
    this.http.post(`${this.api}/especialidades`, { nombre: this.nombreEspecialidad }).subscribe({
      next: () => { this.nombreEspecialidad = ''; this.cargar(); },
      error: () => (this.error = 'errEsp')
    });
  }

  crearMedico() {
    this.error = '';
    this.http.post(`${this.api}/medicos`, {
      nombre: this.nombreMedico,
      especialidad: { id: this.especialidadId }
    }).subscribe({
      next: () => { this.nombreMedico = ''; this.cargar(); },
      error: () => (this.error = 'errMed')
    });
  }

  eliminarMedico(id: number) {
    if (!confirm(this.i.t('confEliminar'))) return;
    this.error = '';
    this.http.delete(`${this.api}/medicos/${id}`).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'errElim')
    });
  }

  salir() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}