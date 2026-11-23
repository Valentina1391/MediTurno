import { Component } from '@angular/core';
import { Idioma } from '../../services/idioma';

@Component({
  selector: 'app-selector-idioma',
  templateUrl: './selector-idioma.html',
  styleUrl: './selector-idioma.css'
})
export class SelectorIdioma {
  constructor(public i: Idioma) {}

  cambiar(e: Event) {
    this.i.cambiar((e.target as HTMLSelectElement).value);
  }
}