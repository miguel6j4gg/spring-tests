import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ModalComponent } from '../../../shared/components/modal/modal.component';

interface MantenimientoRow {
  id: string;
  tipoCliente: string;
  cliente: string;
  documento: string;
  correo: string;
  telefono: string;
  equipo: string;
  marca: string;
  modelo: string;
  tipo: string;
  horometro: number;
  fechaSolicitada: string;
  descripcion: string;
  estado: string;
}

@Component({
  selector: 'app-mantenimiento-list',
  standalone: true,
  imports: [RouterLink, ModalComponent],
  templateUrl: './mantenimiento-list.html',
  styleUrl: './mantenimiento-list.css'
})
export class MantenimientoList {
  registros: MantenimientoRow[] = [
    {
      id: 'MNT-001',
      tipoCliente: 'Empresa',
      cliente: 'Textil Los Andes S.A.',
      documento: '20111222333',
      correo: 'mantenimiento@losandes.pe',
      telefono: '987445566',
      equipo: 'Generador trifásico',
      marca: 'Cummins',
      modelo: 'C250D5',
      tipo: 'Preventivo',
      horometro: 1250.5,
      fechaSolicitada: '2026-11-20',
      descripcion: 'Mantenimiento preventivo de 250 horas, cambio de filtros y aceite.',
      estado: 'Programado'
    },
    {
      id: 'MNT-002',
      tipoCliente: 'Empresa',
      cliente: 'Clínica San Martín',
      documento: '20444555666',
      correo: 'infraestructura@sanmartin.pe',
      telefono: '912887766',
      equipo: 'Generador trifásico',
      marca: 'Perkins',
      modelo: 'P200',
      tipo: 'Correctivo',
      horometro: 3420,
      fechaSolicitada: '2026-11-18',
      descripcion: 'Equipo no arranca, posible falla en batería y precalentador.',
      estado: 'En atención'
    },
    {
      id: 'MNT-003',
      tipoCliente: 'Persona natural',
      cliente: 'Pesquera del Sur',
      documento: '20777888999',
      correo: 'operaciones@pesquerasur.pe',
      telefono: '956123789',
      equipo: 'Torre de iluminación',
      marca: 'Atlas Copco',
      modelo: 'HiLight V5',
      tipo: 'Preventivo',
      horometro: 800,
      fechaSolicitada: '2026-11-25',
      descripcion: 'Revisión de luminarias y motor auxiliar.',
      estado: 'Pendiente'
    }
  ];

  seleccionado: MantenimientoRow | null = null;

  ver(item: MantenimientoRow): void {
    this.seleccionado = item;
  }

  cerrar(): void {
    this.seleccionado = null;
  }
}
