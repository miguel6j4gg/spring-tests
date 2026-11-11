import { Component } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  AbstractControl,
  ValidationErrors
} from '@angular/forms';
import { RouterLink } from '@angular/router';

function validarDocumento(control: AbstractControl): ValidationErrors | null {
  const valor = String(control.value ?? '');

  if (!valor) return null;

  return /^(\d{8}|\d{11})$/.test(valor)
    ? null
    : { documentoInvalido: true };
}

@Component({
  selector: 'app-cotizacion',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './cotizacion.html',
  styleUrl: './cotizacion.css'
})
export class Cotizacion {

  enviado = false;

  private fb = new FormBuilder();

  formulario = this.fb.group({
    tipoCliente: ['empresa', Validators.required],

    nombreCliente: ['', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100)
    ]],

    documento: ['', [
      Validators.required,
      validarDocumento
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

    tipoEquipo: ['', Validators.required],

    potencia: [null as number | null, [
      Validators.required,
      Validators.min(1),
      Validators.max(2000)
    ]],

    fase: ['', Validators.required],

    aplicacion: ['', Validators.required],

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
      tipoSolicitud: 'COTIZACION DE VENTA',
      fechaRegistro: new Date().toISOString(),
      ...this.formulario.getRawValue()
    };

    console.log('===== SOLICITUD DE COTIZACIÓN =====');
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