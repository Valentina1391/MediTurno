import { Component, AfterViewInit, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { Idioma } from '../../services/idioma';
import { SelectorIdioma } from '../../components/selector-idioma/selector-idioma';

declare const google: any;

@Component({
  selector: 'app-login',
  imports: [FormsModule, SelectorIdioma],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements AfterViewInit {
  email = '';
  password = '';
  error = '';

  constructor(private auth: Auth, private router: Router, public i: Idioma, private zona: NgZone) {}

  ngAfterViewInit() {
    const iniciar = () => {
      if (typeof google === 'undefined') { setTimeout(iniciar, 300); return; }
      google.accounts.id.initialize({
        client_id: '660239589647-lemjp8n5rirtgi8igt859djmr92rivel.apps.googleusercontent.com',
        callback: (resp: any) => this.zona.run(() => this.conGoogle(resp.credential))
      });
      google.accounts.id.renderButton(
        document.getElementById('boton-google'),
        { theme: 'outline', size: 'large', width: 340 }
      );
    };
    iniciar();
  }

  conGoogle(credential: string) {
    this.error = '';
    this.auth.loginGoogle(credential).subscribe({
      next: () => this.irSegunRol(),
      error: () => (this.error = 'errorGoogle')
    });
  }

  entrar() {
    this.error = '';
    this.auth.login(this.email, this.password).subscribe({
      next: () => this.irSegunRol(),
      error: () => (this.error = 'errorLogin')
    });
  }

  private irSegunRol() {
    const rol = this.auth.rol;
    if (rol === 'ADMIN') this.router.navigate(['/admin']);
    else if (rol === 'MEDICO') this.router.navigate(['/agenda']);
    else this.router.navigate(['/citas']);
  }
}