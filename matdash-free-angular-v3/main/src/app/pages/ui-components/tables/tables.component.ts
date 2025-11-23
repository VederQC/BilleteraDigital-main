import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { WalletService, Wallet } from '../../../providers/services/wallet/wallet.service';
import { AuthService } from '../../../providers/services/auth/auth.service';
import { jwtDecode } from 'jwt-decode';
import { WalletDialogComponent } from '../../ui-components/wallet/wallet-dialog.component';
import { MatIconModule } from '@angular/material/icon';

import { Router } from '@angular/router';
import { BankOperationsService } from 'src/app/providers/services/bank/bank-operations.service';

@Component({
  selector: 'app-tables',
  standalone: true,
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.css'],
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatDialogModule,
    MatIconModule
  ]
})
export class AppTablesComponent implements OnInit {
  wallet?: Wallet;
  userId: number | null = null;

  banks: any[] = []; // ⭐ Lista de bancos
  bankProgress: any[] = []; // ⭐ Porcentajes calculados

  displayedColumns = ['id', 'userId', 'balance', 'currency', 'createdAt', 'actions'];

  constructor(
    private walletService: WalletService,
    private authService: AuthService,
    private dialog: MatDialog,
    private bankOps: BankOperationsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadWallet();
    this.loadBanks();
  }

  // ============================================================
  // 🔹 CARGAR BANCOS
  // ============================================================
  private loadBanks(): void {
    this.bankOps.getBanks$().subscribe({
      next: (data) => {
        this.banks = data;
        this.calculateFakeProgress(); // ⭐ Generamos porcentajes temporales
      },
      error: (err) => console.error('Error al cargar bancos:', err)
    });
  }

  goToBank(bankId: number): void {
    this.router.navigate(['/app/ui-components/bank', bankId]);
  }

  // ============================================================
  // 🔹 CARGAR WALLET
  // ============================================================
  private loadWallet(): void {
    const token = this.authService.getToken();
    if (!token) return;

    const decoded: any = jwtDecode(token);
    this.userId = Number(decoded.id || decoded.sub);

    if (!isNaN(this.userId!)) {
      this.walletService.getWalletByUserId$(this.userId!).subscribe({
        next: (data) => {
          this.wallet = data;
          this.calculateFakeProgress(); // ⭐ recalcular si wallet cambia
        },
        error: () => (this.wallet = undefined)
      });
    }
  }

  // ============================================================
  // ⭐ CALCULAR PORCENTAJES (FAKES TEMPORALES)
  // ============================================================
  private calculateFakeProgress(): void {
    if (!this.wallet || this.banks.length === 0) return;

    this.bankProgress = this.banks.map((b) => ({
      bankName: b.name,
      percent: Math.floor(Math.random() * 70) + 10 // 10% - 80%
    }));
  }

  // ============================================================
  // 🔹 CREAR WALLET
  // ============================================================
  openCreateDialog(): void {
    const dialogRef = this.dialog.open(WalletDialogComponent, {
      width: '400px',
      data: { userId: this.userId }
    });

    dialogRef.afterClosed().subscribe((created) => {
      if (created) this.loadWallet();
    });
  }

  // ============================================================
  // 🔹 ELIMINAR WALLET
  // ============================================================
  deleteWallet(): void {
    if (!this.userId) return;

    this.walletService.deleteWalletByUserId$(this.userId).subscribe({
      next: () => {
        alert('🗑️ Billetera eliminada correctamente');
        this.wallet = undefined;
      },
      error: (err) => {
        console.error(err);
        alert('❌ Error al eliminar billetera');
      }
    });
  }
}