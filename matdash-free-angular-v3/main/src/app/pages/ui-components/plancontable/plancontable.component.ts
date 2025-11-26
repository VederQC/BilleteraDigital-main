import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PcgeAccount, PcgeService } from 'src/app/providers/services/planContable/pcge.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-plancontable',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './plancontable.component.html',
  styleUrl: './plancontable.component.css'
})
export class PlancontableComponent implements OnInit {

  accounts: PcgeAccount[] = [];
  filteredAccounts: PcgeAccount[] = [];
  searchText: string = '';
  selectedElement: string = '';

  constructor(private pcgeService: PcgeService) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts() {
    this.pcgeService.getAll().subscribe({
      next: res => {
        this.accounts = res;
        this.filteredAccounts = res;
      },
      error: err => console.error(err)
    });
  }

  // FILTRO PRINCIPAL (elemento + búsqueda)
  filterByElement() {
    let data = this.accounts;
    const term = this.searchText.trim().toLowerCase();

    // Filtrar por elemento (primer dígito del código)
    if (this.selectedElement) {
      data = data.filter(acc => acc.code.startsWith(this.selectedElement));
    }

    // Filtro por código o nombre
    if (term) {
      data = data.filter(acc =>
        acc.code.toLowerCase().includes(term) ||
        acc.name.toLowerCase().includes(term)
      );
    }

    this.filteredAccounts = data;
  }

  onSearch() {
    this.filterByElement();
  }
}
