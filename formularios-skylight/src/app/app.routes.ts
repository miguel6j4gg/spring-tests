import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'inicio',
    pathMatch: 'full'
  },
  {
    path: 'inicio',
    loadComponent: () =>
      import('./pages/inicio/inicio').then(m => m.Inicio)
  },
  {
    path: 'cotizacion',
    loadComponent: () =>
      import('./pages/cotizacion/cotizacion').then(m => m.Cotizacion)
  },
  {
    path: 'alquiler',
    loadComponent: () =>
      import('./pages/alquiler/alquiler').then(m => m.Alquiler)
  },
  {
    path: 'mantenimiento',
    loadComponent: () =>
      import('./pages/mantenimiento/mantenimiento').then(m => m.Mantenimiento)
  },
  {
    path: '**',
    redirectTo: 'inicio'
  }
];