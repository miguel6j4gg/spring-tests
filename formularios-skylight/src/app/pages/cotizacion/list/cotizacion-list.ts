import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ModalComponent } from '../../../shared/components/modal/modal.component';

interface CotizacionRow {
  id: string;
  tipoCliente: string;
  cliente: string;
  documento: string;
  contacto: string;
  correo: string;
  telefono: string;
  equipo: string;
  potencia: number;
  fase: string;
  aplicacion: string;
  descripcion: string;
  fecha: string;
  estado: string;
}

@Component({
  selector: 'app-cotizacion-list',
  standalone: true,
  imports: [RouterLink, ModalComponent],
  templateUrl: './cotizacion-list.html',
  styleUrl: './cotizacion-list.css'
})
export class CotizacionList {
  registros: CotizacionRow[] = [
    {
      id: 'COT-001',
      tipoCliente: 'Empresa',
      cliente: 'Minera Andina S.A.C.',
      documento: '20123456789',
      contacto: 'Carlos Ramos',
      correo: 'contacto@mineraandina.pe',
      telefono: '987654321',
      equipo: 'Grupo electrógeno',
      potencia: 500,
      fase: 'Trifásico',
      aplicacion: 'Respaldo eléctrico',
      descripcion: 'Grupo electrógeno de 500 kW para respaldo de planta concentradora.',
      fecha: '2026-11-10',
      estado: 'Pendiente'
    },
    {
      id: 'COT-002',
      tipoCliente: 'Empresa',
      cliente: 'Constructora Vial Sur',
      documento: '20567894321',
      contacto: 'María Torres',
      correo: 'logistica@vialsur.pe',
      telefono: '912345678',
      equipo: 'Torre de iluminación',
      potencia: 20,
      fase: 'Monofásico',
      aplicacion: 'Construcción',
      descripcion: 'Dos torres de iluminación para obra nocturna en carretera.',
      fecha: '2026-11-12',
      estado: 'Atendida'
    },
    {
      id: 'COT-003',
      tipoCliente: 'Persona natural',
      cliente: 'Juan Pérez',
      documento: '45678912',
      contacto: 'Juan Pérez',
      correo: 'juan.perez@gmail.com',
      telefono: '923456789',
      equipo: 'Grupo electrógeno',
      potencia: 100,
      fase: 'Trifásico',
      aplicacion: 'Uso industrial',
      descripcion: 'Equipo de 100 kW para taller industrial en Ate.',
      fecha: '2026-11-15',
      estado: 'Pendiente'
    }
  ];

  seleccionado: CotizacionRow | null = null;

  ver(item: CotizacionRow): void {
    this.seleccionado = item;
  }

  cerrar(): void {
    this.seleccionado = null;
  }
}
