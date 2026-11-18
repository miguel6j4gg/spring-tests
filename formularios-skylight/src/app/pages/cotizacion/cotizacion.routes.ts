import { Routes } from '@angular/router';

export const COTIZACION_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./list/cotizacion-list').then((m) => m.CotizacionList)
  },
  {
    path: 'registrar',
    loadComponent: () =>
      import('./form/cotizacion-form').then((m) => m.CotizacionForm)
  }
];
