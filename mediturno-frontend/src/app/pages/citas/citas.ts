import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { Idioma } from '../../services/idioma';
import { SelectorIdioma } from '../../components/selector-idioma/selector-idioma';

@Component({
  selector: 'app-citas',
  imports: [FormsModule, SelectorIdioma],
  templateUrl: './citas.html',
  styleUrl: './citas.css'
})
export class Citas implements OnInit {
  private api = 'https://localhost:8443/api';
  medicos: any[] = [];
  citas: any[] = [];
  medicoId: number | null = null;
  fechaHora = '';
  mensaje = '';
  error = '';
  ahora = new Date().toISOString().slice(0, 16);

  get programadas() { return this.citas.filter(c => c.estado === 'PROGRAMADA').length; }
  get atendidas()   { return this.citas.filter(c => c.estado === 'ATENDIDA').length; }
  get canceladas()  { return this.citas.filter(c => c.estado === 'CANCELADA').length; }

  constructor(private http: HttpClient, public auth: Auth, private router: Router, public i: Idioma) {}

  ngOnInit() {
    this.cargarMedicos();
    this.cargarCitas();
  }

  cargarMedicos() {
    this.http.get<any[]>(`${this.api}/medicos`).subscribe(m => (this.medicos = m));
  }

  cargarCitas() {
    this.http.get<any[]>(`${this.api}/citas/paciente/${this.auth.id}`)
      .subscribe(c => (this.citas = c));
  }

  reservar() {
    this.mensaje = '';
    this.error = '';
    const cuerpo = {
      medico: { id: this.medicoId },
      paciente: { id: this.auth.id },
      fechaHora: this.fechaHora + ':00'
    };
    this.http.post(`${this.api}/citas`, cuerpo).subscribe({
      next: () => {
        this.mensaje = 'exito';
        this.cargarCitas();
      },
      error: e => (this.error = e.status === 409 ? 'conflicto' : 'fallo')
    });
  }

  cancelar(id: number) {
    if (!confirm(this.i.t('confCancelar'))) return;
    this.http.put(`${this.api}/citas/${id}/cancelar`, {}).subscribe(() => this.cargarCitas());
  }

  salir() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}