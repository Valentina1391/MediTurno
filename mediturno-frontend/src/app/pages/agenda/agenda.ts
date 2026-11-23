import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { Idioma } from '../../services/idioma';
import { SelectorIdioma } from '../../components/selector-idioma/selector-idioma';

declare const $: any;

@Component({
  selector: 'app-agenda',
  imports: [SelectorIdioma],
  templateUrl: './agenda.html',
  styleUrl: './agenda.css'
})
export class Agenda implements OnInit {
  private api = 'https://localhost:8443/api/citas';
  citas = signal<any[]>([]);
  mes = signal(new Date(new Date().getFullYear(), new Date().getMonth(), 1));
  seleccionado = signal<string | null>(null);

  constructor(private http: HttpClient, public auth: Auth, private router: Router, public i: Idioma) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    this.http.get<any[]>(`${this.api}/mi-agenda`).subscribe(c => this.citas.set(c));
  }

  atender(id: number) {
    this.http.put(`${this.api}/${id}/estado?estado=ATENDIDA`, {}).subscribe(() => this.cargar());
  }

  filtrar(texto: string) {
    $('table tbody tr').each(function (this: HTMLElement) {
      const visible = $(this).text().toLowerCase().includes(texto.toLowerCase());
      $(this).toggle(visible);
    });
  }

  // ---------- Calendario ----------

  /** Título del mes */
  get tituloMes() {
    return this.mes().toLocaleDateString(this.i.lang(), { month: 'long', year: 'numeric' });
  }

  /** Nombres cortos de los días */
  get diasSemana() {
    return [0, 1, 2, 3, 4, 5, 6].map(k =>
      new Date(2024, 0, 1 + k).toLocaleDateString(this.i.lang(), { weekday: 'short' }));
  }

  /** Celdas del mes: huecos al inicio y un objeto por cada día */
  get celdas(): any[] {
    const m = this.mes();
    const y = m.getFullYear();
    const mo = m.getMonth();
    const vacios = (new Date(y, mo, 1).getDay() + 6) % 7; // la semana empieza en lunes
    const total = new Date(y, mo + 1, 0).getDate();
    const lista: any[] = [];
    for (let k = 0; k < vacios; k++) lista.push(null);
    for (let d = 1; d <= total; d++) {
      const fecha = `${y}-${String(mo + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      lista.push({
        dia: d,
        fecha,
        citas: this.citas().filter(c => c.fechaHora.startsWith(fecha))
      });
    }
    return lista;
  }

  get hoy() {
    const h = new Date();
    return `${h.getFullYear()}-${String(h.getMonth() + 1).padStart(2, '0')}-${String(h.getDate()).padStart(2, '0')}`;
  }

  /** Citas del día seleccionado. */
  get citasDelDia() {
    const f = this.seleccionado();
    return f ? this.citas().filter(c => c.fechaHora.startsWith(f)) : [];
  }

  cambiarMes(n: number) {
    const m = this.mes();
    this.mes.set(new Date(m.getFullYear(), m.getMonth() + n, 1));
    this.seleccionado.set(null);
  }

  irHoy() {
    const h = new Date();
    this.mes.set(new Date(h.getFullYear(), h.getMonth(), 1));
    this.seleccionado.set(this.hoy);
  }

  salir() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}