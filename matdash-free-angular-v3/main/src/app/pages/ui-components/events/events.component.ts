import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogRef, MatDialogModule } from '@angular/material/dialog';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';

import { Event } from 'src/app/providers/models/event.model';
import { EventService } from 'src/app/providers/services/events/events.service';
import { AuthService } from 'src/app/providers/services/auth/auth.service';

@Component({
  selector: 'app-events',
  standalone: true,
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule
  ]
})
export class EventsComponent implements OnInit {

  @ViewChild('dialogTemplate') dialogTemplate: any;
  dialogRef!: MatDialogRef<any>;

  events: Event[] = [];
  editingId: number | null = null;

  // 🔥 YA NO HARDCODEADO
  userId!: number;

  form = {
    name: '',
    description: '',
    budget: 0,
    startDate: '',
    endDate: ''
  };

  displayedColumns = ['name', 'budget', 'spent', 'startDate', 'endDate', 'actions'];

  constructor(
    private eventService: EventService,
    private dialog: MatDialog,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadUserId();   // ⭐ Obtiene el verdadero ID del usuario logueado
    this.loadEvents();   // ⭐ Carga eventos filtrados por usuario real
  }

  // ============================================================
  // 🔥 OBTENER ID DEL USUARIO DESDE EL TOKEN JWT
  // ============================================================
  loadUserId(): void {
    const token = this.authService.getToken();
    if (!token) return;

    const decoded: any = jwtDecode(token);
    this.userId = Number(decoded.id || decoded.sub);

    console.log("USER ID DESDE TOKEN:", this.userId);
  }

  // ============================================================
  // 🔹 Cargar eventos
  // ============================================================
  loadEvents(): void {
    if (!this.userId) return;

    this.eventService.getEventsByUser$(this.userId).subscribe({
      next: (list) => (this.events = list)
    });
  }

  // 🟣 Abrir para CREAR
  openDialog(): void {
    this.editingId = null;
    this.form = { name: '', description: '', budget: 0, startDate: '', endDate: '' };
    this.dialogRef = this.dialog.open(this.dialogTemplate);
  }

  // 🟡 Abrir para EDITAR
  openEditDialog(event: Event): void {
    this.editingId = event.id;

    this.form = {
      name: event.name,
      description: event.description,
      budget: event.budget,
      startDate: event.startDate,
      endDate: event.endDate
    };

    this.dialogRef = this.dialog.open(this.dialogTemplate);
  }

  // 🟢 Guardar CREAR o EDITAR
  saveEvent(): void {
    const payload = {
      ...this.form,
      userId: this.userId
    };

    if (this.editingId === null) {
      this.eventService.createEvent$(payload).subscribe({
        next: () => {
          this.loadEvents();
          this.dialogRef.close();
        }
      });

    } else {
      this.eventService.updateEvent$(this.editingId, payload).subscribe({
        next: () => {
          this.loadEvents();
          this.dialogRef.close();
        }
      });
    }
  }

  // ➕ AGREGAR GASTO
  addSpent(event: Event): void {
    const amount = prompt('Monto a agregar:');

    if (!amount) return;

    const num = Number(amount);
    if (isNaN(num)) return alert('Monto inválido');

    this.eventService.updateSpent$(event.id, { eventId: event.id, amount: num })
      .subscribe({
        next: () => this.loadEvents()
      });
  }

  // 🗑️ Eliminar
  deleteEvent(id: number): void {
    if (!confirm('¿Eliminar este evento?')) return;

    this.eventService.deleteEvent$(id).subscribe({
      next: () => this.loadEvents()
    });
  }
}