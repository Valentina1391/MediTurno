import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Idioma } from '../../services/idioma';
import { SelectorIdioma } from '../../components/selector-idioma/selector-idioma';

@Component({
  selector: 'app-inicio',
  imports: [RouterLink, SelectorIdioma],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio {
  constructor(public i: Idioma) {}
}