import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Idioma } from './services/idioma';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  constructor(public i: Idioma) {}
}