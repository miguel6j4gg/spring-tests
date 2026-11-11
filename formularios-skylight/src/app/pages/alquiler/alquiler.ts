import { Component } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  AbstractControl,
  ValidationErrors,
  ValidatorFn
} from '@angular/forms';
import { RouterLink } from '@angular/router';

const validarPeriodoAlquiler: ValidatorFn = (
  control: AbstractControl
): ValidationErrors | null => {

  const inicio = control.get('fechaInicio')?.value;
  const fin = control.get('fechaFin')?.value;

  if (!inicio || !fin) return null;

  return new Date(fin + 'T00:00:00') >
         new Date(inicio + 'T00:00:00')
    ? null
    : { periodoInvalido: true };
};

@Component({
  selector: 'app-alquiler',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './alquiler.html',
  styleUrl: './alquiler.css'
})
export class Alquiler {

  enviado = false;

  private fb = new FormBuilder();

  hoy = new Date().toLocaleDateString('en-CA');

  formulario = this.fb.group({
    nombreEmpresa: ['', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100)
    ]],

    ruc: ['', [
      Validators.required,
      Validators.pattern(/^\d{11}$/)
    ]],

    contacto: ['', [
      Validators.required,
      Validators.minLength(3)
    ]],

    correo: ['', [
      Validators.required,
      Validators.email
    ]],

    telefono: ['', [
      Validators.required,
      Validators.pattern(/^9\d{8}$/)
    ]],

    potencia: [null as number | null, [
      Validators.required,
      Validators.min(1),
      Validators.max(2000)
    ]],

    tipoGenerador: ['', Validators.required],

    fechaInicio: ['', Validators.required],

    fechaFin: ['', Validators.required],

    modalidad: ['', Validators.required],

    ubicacion: ['', [
      Validators.required,
      Validators.minLength(5)
    ]],

    descripcion: ['', [
      Validators.required,
      Validators.minLength(10),
      Validators.maxLength(500)
    ]]

  }, { validators: validarPeriodoAlquiler });

  enviar(): void {

    this.enviado = false;

    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const datos = {
      tipoSolicitud: 'SOLICITUD DE ALQUILER',
      fechaRegistro: new Date().toISOString(),
      ...this.formulario.getRawValue()
    };

    console.log('===== SOLICITUD DE ALQUILER =====');
    console.log(datos);

    this.enviado = true;

    this.formulario.reset();
  }

  tieneError(campo: string): boolean {
    const control = this.formulario.get(campo);
    return !!control && control.invalid && control.touched;
  }

  get periodoInvalido(): boolean {
    return !!this.formulario.errors?.['periodoInvalido']
      && (this.formulario.get('fechaInicio')?.touched ?? false)
      && (this.formulario.get('fechaFin')?.touched ?? false);
  }
}