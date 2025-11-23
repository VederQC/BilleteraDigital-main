import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

import { Bank } from 'src/app/providers/models/bank.model';
import { UserBankIncome } from 'src/app/providers/models/user-bank-income.model';
import { UserBankBalance } from 'src/app/providers/models/user-bank-balance.model';
import { BankOperationsService } from 'src/app/providers/services/bank/bank-operations.service';

@Component({
  selector: 'app-bank-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule
  ],
  templateUrl: './bank-detail.component.html',
  styleUrls: ['./bank-detail.component.css']
})
export class BankDetailComponent implements OnInit {

  bankId!: number;
  bank?: Bank;
  incomes: UserBankIncome[] = [];

  // ⭐ Saldo total del banco
  bankBalance: number = 0;

  // FORMULARIOS
  showAddForm = false;
  showTransferForm = false;

  amount: number = 0;
  description: string = '';
  transferAmount: number = 0;

  constructor(
    private route: ActivatedRoute,
    private bankOps: BankOperationsService
  ) {}

  ngOnInit(): void {
    this.bankId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadBankData();
  }

  loadBankData(): void {
    // ➤ Banco
    this.bankOps.getBankById$(this.bankId).subscribe({
      next: (bank) => this.bank = bank
    });

    // ➤ Historial
    this.bankOps.getIncomeHistory$(8, this.bankId).subscribe({
      next: (data) => this.incomes = data
    });

    // ⭐ ➤ SALDO TOTAL (YA FUNCIONA PORQUE EL BACKEND YA TIENE EL ENDPOINT)
    this.bankOps.getBankBalance$(8, this.bankId).subscribe({
      next: (data: UserBankBalance) => {
        this.bankBalance = Number(data.balance);
      }
    });
  }

  toggleAddForm() {
    this.showAddForm = !this.showAddForm;
    this.showTransferForm = false;
  }

  toggleTransferForm() {
    this.showTransferForm = !this.showTransferForm;
    this.showAddForm = false;
  }

  // GUARDAR INGRESO
  submitIncome() {
    if (!this.amount || this.amount <= 0) {
      alert("Monto inválido");
      return;
    }

    if (!this.description.trim()) {
      alert("Descripción requerida");
      return;
    }

    this.bankOps.addIncomeToBank$({
      userId: 8,
      bankId: this.bankId,
      amount: this.amount,
      description: this.description
    }).subscribe({
      next: () => {
        this.loadBankData();  
        this.showAddForm = false;
        this.amount = 0;
        this.description = '';
      }
    });
  }

  // TRANSFERIR
  submitTransfer() {
    if (!this.transferAmount || this.transferAmount <= 0) {
      alert("Monto inválido");
      return;
    }

    this.bankOps.transferToWallet$({
      userId: 8,
      bankId: this.bankId,
      amount: this.transferAmount
    }).subscribe({
      next: () => {
        this.loadBankData();  
        this.showTransferForm = false;
        this.transferAmount = 0;
      }
    });
  }
}
