import { Routes } from '@angular/router';

export const ALQUILER_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./list/alquiler-list').then((m) => m.AlquilerList)
  },
  {
    path: 'registrar',
    loadComponent: () =>
      import('./form/alquiler-form').then((m) => m.AlquilerForm)
  }
];
