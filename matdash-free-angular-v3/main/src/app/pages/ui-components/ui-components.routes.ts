import { Routes } from '@angular/router';
import { AppMenuComponent } from './menu/menu.component';

export const UiComponentsRoutes: Routes = [
  {
    path: '',
    children: [

      {
        path: '',
        redirectTo: 'wallet',
        pathMatch: 'full',
      },

      // 🟢 WALLET
      {
        path: 'wallet',
        loadComponent: () =>
          import('src/app/pages/ui-components/tables/tables.component').then(
            m => m.AppTablesComponent
          ),
      },

      // 🟢 BANK DETAIL (RUTA CORRECTA)
      {
        path: 'bank/:id',
        loadComponent: () =>
          import('src/app/pages/ui-components/banks/bank-detail.component').then(
            m => m.BankDetailComponent
          ),
      },

      // 🟢 TRANSACTIONS
      {
        path: 'transactions',
        loadComponent: () =>
          import('./transactions/transactions.component').then(
            m => m.TransactionsComponent
          ),
      },

      // 🟢 EVENTS
      {
        path: 'events',
        loadComponent: () =>
          import('./events/events.component').then(
            m => m.EventsComponent
          ),
      },

      // 🟢 GOALS
      {
        path: 'goals',
        loadComponent: () =>
          import('./goals/goals.component').then(
            m => m.GoalsComponent
          ),
      },

      // 🟢 MENU
      {
        path: 'menu',
        component: AppMenuComponent
      },

      // 🟢 SUBCATEGORIAS
      {
        path: 'subcategorias',
        loadComponent: () =>
          import('./subcategorias/subcategorias.component').then(
            m => m.AppSubcategoriasComponent
          ),
      },
    ],
  },
];
