import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { TransactionService, TransactionType } from '../../../providers/services/transaction/transaction.service';

@Component({
  selector: 'app-transaction-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  template: `
    <h2 mat-dialog-title>Nueva Transacción</h2>

    <mat-dialog-content>
      <form [formGroup]="txForm">
        <mat-form-field appearance="outline" class="w-100">
          <mat-label>Tipo</mat-label>
          <mat-select formControlName="type">
            <mat-option value="INCOME">Ingreso</mat-option>
            <mat-option value="EXPENSE">Gasto</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline" class="w-100">
          <mat-label>Categoría (ID)</mat-label>
          <input matInput type="number" formControlName="categoryId" />
        </mat-form-field>

        <mat-form-field appearance="outline" class="w-100">
          <mat-label>Subcategoría (ID)</mat-label>
          <input matInput type="number" formControlName="subcategoryId" />
        </mat-form-field>

        <mat-form-field appearance="outline" class="w-100">
          <mat-label>Evento (ID opcional)</mat-label>
          <input matInput type="number" formControlName="eventId" />
        </mat-form-field>

        <mat-form-field appearance="outline" class="w-100">
          <mat-label>Monto</mat-label>
          <input matInput type="number" formControlName="amount" />
        </mat-form-field>

        <mat-form-field appearance="outline" class="w-100">
          <mat-label>Descripción</mat-label>
          <input matInput formControlName="description" />
        </mat-form-field>
      </form>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancelar</button>
      <button mat-raised-button color="primary" (click)="onSubmit()" [disabled]="txForm.invalid">
        Guardar
      </button>
    </mat-dialog-actions>
  `
})
export class TransactionDialogComponent {
  txForm: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { userId: number },
    private fb: FormBuilder,
    private txService: TransactionService,
    private dialogRef: MatDialogRef<TransactionDialogComponent>
  ) {
    this.txForm = this.fb.group({
      type: ['EXPENSE' as TransactionType, Validators.required],
      categoryId: [null, Validators.required],
      subcategoryId: [null, Validators.required],
      eventId: [null],
      amount: [null, [Validators.required, Validators.min(0.01)]],
      description: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.txForm.invalid) return;

    const formValue = this.txForm.value;
    const payload = {
      userId: this.data.userId,
      categoryId: Number(formValue.categoryId),
      subcategoryId: Number(formValue.subcategoryId),
      eventId: formValue.eventId ? Number(formValue.eventId) : null,
      type: formValue.type,
      amount: Number(formValue.amount),
      description: formValue.description
    };

    this.txService.createTransaction$(payload).subscribe({
      next: () => {
        alert('✅ Transacción registrada correctamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error al crear transacción:', err);
        alert('❌ Error al crear transacción');
      }
    });
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
