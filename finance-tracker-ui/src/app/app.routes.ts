import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';

export const routes: Routes = [
  { 
    path: 'login', 
    component: LoginComponent 
  },
  { 
    path: '', 
    redirectTo: '/login', 
    pathMatch: 'full' // When the user visits 'localhost:4200/', force them to login
  },
  { 
    path: '**', 
    redirectTo: '/login' // Wildcard route: catches typos like 'localhost:4200/lognn'
  }
];