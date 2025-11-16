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

  displayedColumns = ['id', 'userId', 'balance', 'currency', 'createdAt', 'actions'];

  constructor(
    private walletService: WalletService,
    private authService: AuthService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadWallet();
  }

  private loadWallet(): void {
    const token = this.authService.getToken();
    if (!token) return;

    const decoded: any = jwtDecode(token);
    this.userId = Number(decoded.id || decoded.sub);

    if (this.userId && !isNaN(this.userId)) {
      this.walletService.getWalletByUserId$(this.userId).subscribe({
        next: (data) => (this.wallet = data),
        error: () => (this.wallet = undefined)
      });
    }
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(WalletDialogComponent, {
      width: '400px',
      data: { userId: this.userId }
    });

    dialogRef.afterClosed().subscribe((created) => {
      if (created) this.loadWallet(); // recarga la tabla si se creó
    });
  }

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
