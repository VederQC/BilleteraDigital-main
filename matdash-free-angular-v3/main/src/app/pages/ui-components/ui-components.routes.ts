import { Routes } from '@angular/router';
import {AppMenuComponent} from "./menu/menu.component";
import {AppSubcategoriasComponent} from "./subcategorias/subcategorias.component";

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
            (m) => m.AppTablesComponent
          ),
      },

      // 🟢 TRANSACTIONS (nombre correcto)
      {
        path: 'transactions',
        loadComponent: () =>
          import('./transactions/transactions.component').then(
            (m) => m.TransactionsComponent
          ),
      },

      // 🟢 EVENTS
      {
        path: 'events',
        loadComponent: () =>
          import('./events/events.component').then(
            (m) => m.EventsComponent
          ),
      },

      // 🟢 GOALS
      {
        path: 'goals',
        loadComponent: () =>
          import('./goals/goals.component').then(
            (m) => m.GoalsComponent
          ),
      },
      {
        path: 'menu',
        loadComponent: () =>
          import('./menu/menu.component').then(
            (m) => m.AppMenuComponent
          ),
      },
      {
        path: 'subcategorias',
        loadComponent: () =>
          import('./subcategorias/subcategorias.component')
            .then((m) => m.AppSubcategoriasComponent),
      },

    ],
  },
];
