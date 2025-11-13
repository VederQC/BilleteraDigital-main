import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { WalletService, Wallet } from '../../../providers/services/wallet/wallet.service';
import {CommonModule} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";



@Component({
  selector: 'app-wallet-crud',
  templateUrl: './wallet-crud.component.html',
  imports: [
    CommonModule,
    ReactiveFormsModule ,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class WalletCrudComponent {
  walletForm: FormGroup;
  wallet?: Wallet;

  constructor(private fb: FormBuilder, private walletService: WalletService) {
    this.walletForm = this.fb.group({
      userId: ['', Validators.required],
      currency: ['pen', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.walletForm.invalid) return;

    const walletData = this.walletForm.value;
    this.walletService.createWallet$(walletData).subscribe({
      next: (wallet) => {
        console.log('Wallet creada:', wallet);
        this.wallet = wallet;
        alert('✅ Wallet creada correctamente');
      },
      error: (err) => {
        console.error('Error al crear wallet:', err);
        alert('❌ Error al crear wallet');
      },
    });
  }

  onDelete(): void {
    const userId = this.walletForm.get('userId')?.value;
    if (!userId) return;

    this.walletService.deleteWalletByUserId$(userId).subscribe({
      next: () => {
        console.log('Wallet eliminada');
        alert('🗑️ Wallet eliminada correctamente');
        this.wallet = undefined;
      },
      error: (err) => {
        console.error('Error al eliminar wallet:', err);
        alert('❌ Error al eliminar wallet');
      },
    });
  }
}
