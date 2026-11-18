import { Routes } from '@angular/router';

export const MANTENIMIENTO_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./list/mantenimiento-list').then((m) => m.MantenimientoList)
  },
  {
    path: 'registrar',
    loadComponent: () =>
      import('./form/mantenimiento-form').then((m) => m.MantenimientoForm)
  }
];
