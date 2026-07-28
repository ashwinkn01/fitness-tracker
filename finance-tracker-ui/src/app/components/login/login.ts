import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss' // Note: Angular 17+ uses styleUrl (singular)
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage = '';

  // inject() is the modern Angular way to bring in services
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  constructor() {
    // This creates our form and enforces that both fields are mandatory
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      // Remember that Observable? This .subscribe() is what actually fires the HTTP request
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          this.authService.setToken(response.accessToken);
          // Once successful, send them to the dashboard
          this.router.navigate(['/dashboard']); 
        },
        error: (err) => {
          this.errorMessage = 'Invalid username or password. Please try again.';
        }
      });
    }
  }
}