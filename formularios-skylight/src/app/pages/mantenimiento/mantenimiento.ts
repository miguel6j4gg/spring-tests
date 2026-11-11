import { Component } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  AbstractControl,
  ValidationErrors
} from '@angular/forms';
import { RouterLink } from '@angular/router';

const fechaNoPasada = (
  control: AbstractControl
): ValidationErrors | null => {

  if (!control.value) return null;

  const fecha = new Date(control.value + 'T00:00:00');
  const hoy = new Date();

  hoy.setHours(0, 0, 0, 0);

  return fecha >= hoy ? null : { fechaPasada: true };
};

@Component({
  selector: 'app-mantenimiento',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './mantenimiento.html',
  styleUrl: './mantenimiento.css'
})
export class Mantenimiento {

  enviado = false;

  private fb = new FormBuilder();

  hoy = new Date().toLocaleDateString('en-CA');

  formulario = this.fb.group({

    tipoCliente: ['empresa', Validators.required],

    nombreCliente: ['', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100)
    ]],

    documento: ['', [
      Validators.required,
      Validators.pattern(/^(\d{8}|\d{11})$/)
    ]],

    correo: ['', [
      Validators.required,
      Validators.email
    ]],

    telefono: ['', [
      Validators.required,
      Validators.pattern(/^9\d{8}$/)
    ]],

    tipoEquipo: ['', Validators.required],

    marca: ['', Validators.required],

    modelo: ['', [
      Validators.required,
      Validators.minLength(2)
    ]],

    tipoMantenimiento: ['', Validators.required],

    horometro: [null as number | null, [
      Validators.required,
      Validators.min(0),
      Validators.max(1000000)
    ]],

    fechaSolicitada: ['', [
      Validators.required,
      fechaNoPasada
    ]],

    descripcion: ['', [
      Validators.required,
      Validators.minLength(10),
      Validators.maxLength(500)
    ]]

  });

  enviar(): void {

    this.enviado = false;

    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const datos = {
      tipoSolicitud: 'SOLICITUD DE MANTENIMIENTO',
      fechaRegistro: new Date().toISOString(),
      ...this.formulario.getRawValue()
    };

    console.log('===== SOLICITUD DE MANTENIMIENTO =====');
    console.log(datos);

    this.enviado = true;

    this.formulario.reset({
      tipoCliente: 'empresa'
    });
  }

  tieneError(campo: string): boolean {
    const control = this.formulario.get(campo);
    return !!control && control.invalid && control.touched;
  }

}