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
    loadChildren: () =>
      import('./pages/cotizacion/cotizacion.routes').then(m => m.COTIZACION_ROUTES)
  },
  {
    path: 'alquiler',
    loadChildren: () =>
      import('./pages/alquiler/alquiler.routes').then(m => m.ALQUILER_ROUTES)
  },
  {
    path: 'mantenimiento',
    loadChildren: () =>
      import('./pages/mantenimiento/mantenimiento.routes').then(m => m.MANTENIMIENTO_ROUTES)
  },
  {
    path: '**',
    redirectTo: 'inicio'
  }
];
