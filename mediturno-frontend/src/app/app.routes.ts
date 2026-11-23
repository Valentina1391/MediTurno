import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Citas } from './pages/citas/citas';
import { Agenda } from './pages/agenda/agenda';
import { Admin } from './pages/admin/admin';
import { authGuard } from './services/auth.guard';
import { Inicio } from './pages/inicio/inicio';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'citas', component: Citas, canActivate: [authGuard] },
  { path: 'agenda', component: Agenda, canActivate: [authGuard] },
  { path: 'admin', component: Admin, canActivate: [authGuard] },
  { path: '', component: Inicio },
];