import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ModalComponent } from '../../../shared/components/modal/modal.component';

interface AlquilerRow {
  id: string;
  empresa: string;
  ruc: string;
  contacto: string;
  correo: string;
  telefono: string;
  potencia: number;
  tipoGenerador: string;
  fechaInicio: string;
  fechaFin: string;
  modalidad: string;
  ubicacion: string;
  descripcion: string;
  estado: string;
}

@Component({
  selector: 'app-alquiler-list',
  standalone: true,
  imports: [RouterLink, ModalComponent],
  templateUrl: './alquiler-list.html',
  styleUrl: './alquiler-list.css'
})
export class AlquilerList {
  registros: AlquilerRow[] = [
    {
      id: 'ALQ-001',
      empresa: 'Obras Civiles Lima S.A.C.',
      ruc: '20123456789',
      contacto: 'Luis Quispe',
      correo: 'operaciones@obrasciviles.pe',
      telefono: '987112233',
      potencia: 150,
      tipoGenerador: 'Trifásico',
      fechaInicio: '2026-12-01',
      fechaFin: '2026-12-31',
      modalidad: 'Con operador',
      ubicacion: 'Av. Universitaria 123, Lima',
      descripcion: 'Alquiler de grupo de 150 kW para obra en Lima Norte.',
      estado: 'Vigente'
    },
    {
      id: 'ALQ-002',
      empresa: 'Eventos del Norte E.I.R.L.',
      ruc: '20987654321',
      contacto: 'Ana Flores',
      correo: 'eventos@norte.pe',
      telefono: '945678123',
      potencia: 60,
      tipoGenerador: 'Monofásico',
      fechaInicio: '2026-11-20',
      fechaFin: '2026-11-25',
      modalidad: 'Máquina seca',
      ubicacion: 'Plaza de Armas, Trujillo',
      descripcion: 'Cobertura eléctrica para festival de 5 días.',
      estado: 'Finalizado'
    },
    {
      id: 'ALQ-003',
      empresa: 'Agroindustrial Santa Rosa',
      ruc: '20345678912',
      contacto: 'Pedro Huamán',
      correo: 'planta@santarosa.pe',
      telefono: '978654321',
      potencia: 350,
      tipoGenerador: 'Trifásico',
      fechaInicio: '2027-01-05',
      fechaFin: '2027-03-05',
      modalidad: 'Con operador',
      ubicacion: 'Panamericana Sur Km 45, Ica',
      descripcion: 'Respaldo de planta de procesamiento por campaña de verano.',
      estado: 'Pendiente'
    }
  ];

  seleccionado: AlquilerRow | null = null;

  ver(item: AlquilerRow): void {
    this.seleccionado = item;
  }

  cerrar(): void {
    this.seleccionado = null;
  }
}
