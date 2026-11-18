import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css'
})
export class ModalComponent {
  @Input() eyebrow = '';
  @Input() title = '';
  @Input() subtitle = '';

  @Output() closed = new EventEmitter<void>();

  cerrar(): void {
    this.closed.emit();
  }
}
