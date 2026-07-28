import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DashboardSummary } from '../models/dashboard';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  // inject() is the modern alternative to constructor injection
  private http = inject(HttpClient);
  
  // This environment variable should point to your Spring Boot URL (e.g., http://localhost:8080/api)
  private apiUrl = `${environment.apiUrl}/dashboard`;

  // Fires a GET request. The JWT Interceptor we built earlier will automatically attach the token!
  getSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.apiUrl}/summary`);
  }
}