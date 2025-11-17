import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';


import { GoalsService } from 'src/app/providers/services/goals/GoalsService';
import { TransactionService, TransactionType } from 'src/app/providers/services/transaction/transaction.service';
import { AuthService } from 'src/app/providers/services/auth/auth.service';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-goals',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatProgressBarModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './goals.component.html',
  styleUrls: ['./goals.component.css']
})
export class GoalsComponent implements OnInit {

  @ViewChild('goalDialog') goalDialog: any;
  @ViewChild('addAmountDialog') addAmountDialog: any;

  dialogRef!: MatDialogRef<any>;

  goals: any[] = [];
  userId!: number;

  editingGoal: any = null;

  form = {
    name: '',
    description: '',
    targetAmount: 0,
    deadline: ''
  };

  selectedGoal: any = null;
  amountToAdd: number = 0;

  constructor(
    private goalsService: GoalsService,
    private txService: TransactionService,
    private authService: AuthService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadUserId();
    this.loadGoals();
  }

  private loadUserId(): void {
    const token = this.authService.getToken();
    if (!token) return;

    const decoded: any = jwtDecode(token);
    this.userId = Number(decoded.id || decoded.sub);
  }

  loadGoals(): void {
    this.goalsService.getGoalsByUser$(this.userId).subscribe({
      next: (res) => this.goals = res,
      error: () => (this.goals = [])
    });
  }

  // =======================
  // CREAR META
  // =======================
  openCreateDialog(): void {
    this.editingGoal = null;
    this.form = {
      name: '',
      description: '',
      targetAmount: 0,
      deadline: ''
    };
    this.dialogRef = this.dialog.open(this.goalDialog);
  }

  saveGoal(): void {
    const payload = {
      userId: this.userId,
      name: this.form.name,
      description: this.form.description,
      targetAmount: this.form.targetAmount,
      currentAmount: this.editingGoal ? this.editingGoal.currentAmount : 0,
      deadline: this.form.deadline
        ? new Date(this.form.deadline).toISOString()
        : null,


      status: 'EN_PROGRESO'
    };

    if (this.editingGoal) {
      this.goalsService.updateGoal$(this.editingGoal.id, payload).subscribe(() => {
        this.loadGoals();
        this.dialogRef.close();
      });
    } else {
      this.goalsService.createGoal$(payload).subscribe(() => {
        this.loadGoals();
        this.dialogRef.close();
      });
    }
  }

  // =======================
  // EDITAR META
  // =======================
  openEditDialog(goal: any): void {
    this.editingGoal = goal;
    this.form = {
      name: goal.name,
      description: goal.description,
      targetAmount: goal.targetAmount,
      deadline: goal.deadline ? goal.deadline.substring(0, 10) : ''
    };
    this.dialogRef = this.dialog.open(this.goalDialog);
  }

  // =======================
  // ELIMINAR META
  // =======================
  confirmDelete(id: number): void {
    if (!confirm('¿Eliminar esta meta?')) return;

    this.goalsService.deleteGoal$(id).subscribe(() => this.loadGoals());
  }

  // =======================
  // APORTAR A UNA META
  // =======================
  openAddAmountDialog(goal: any): void {
    this.selectedGoal = goal;
    this.amountToAdd = 0;
    this.dialogRef = this.dialog.open(this.addAmountDialog);
  }

  addAmount(): void {
    if (this.amountToAdd <= 0) {
      alert('El monto debe ser mayor a 0');
      return;
    }

    const txPayload = {
      userId: this.userId,
      categoryId: 6, // categoría Objetivos
      subcategoryId: this.selectedGoal.subcategoryId, // ✔️ subcategoría real creada automáticamente
      goalId: this.selectedGoal.id, // ✔️ la meta real
      eventId: null,
      type: TransactionType.EXPENSE,
      amount: this.amountToAdd,
      description: `Aporte a meta: ${this.selectedGoal.name}`
    };

    this.txService.createTransaction$(txPayload).subscribe({
      next: () => {
        this.loadGoals();
        this.dialogRef.close();
      },
      error: (err) => {
        console.error(err);
        alert('Error al aportar');
      }
    });
  }

}
