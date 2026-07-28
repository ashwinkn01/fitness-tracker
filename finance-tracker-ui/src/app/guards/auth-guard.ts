import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  // inject() allows us to pull in our services without needing a constructor
  const authService = inject(AuthService);
  const router = inject(Router);

  // Check if the user has a valid, unexpired JWT
  if (authService.isLoggedIn()) {
    return true; // Let them pass
  }

  // If they aren't logged in, kick them back to the login screen
  router.navigate(['/login']);
  return false;
};