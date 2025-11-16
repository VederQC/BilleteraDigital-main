import { Component, EventEmitter, Output, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/providers/services/auth/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  /** Recibe si se debe mostrar o no el botón */
  @Input() showToggle: boolean = true;

  /** Emite el evento hacia FullComponent */
  @Output() toggleCollapsed = new EventEmitter<void>();
  @Output() toggleMobileNav = new EventEmitter<void>();

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onToggleCollapsed() {
    this.toggleCollapsed.emit();
  }

  onToggleMobileNav() {
    this.toggleMobileNav.emit();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login'], { replaceUrl: true });
  }
}
