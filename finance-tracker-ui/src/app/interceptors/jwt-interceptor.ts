import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Inject our AuthService to get access to localStorage
  const authService = inject(AuthService);
  const token = authService.getToken();

  // 2. If a token exists, clone the HTTP request and attach the header
  if (token) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    // 3. Send the modified request on its way to the backend
    return next(clonedReq);
  }

  // 4. If there is no token (e.g., logging in for the first time), just send the normal request
  return next(req);
};