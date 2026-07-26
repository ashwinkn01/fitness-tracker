package com.ashwin.financetracker.finance_tracker_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    // Dependency Injection
    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Look for the "Authorization" header in the incoming request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. If the header is missing or doesn't start with "Bearer ", reject and move on
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the token (skip the first 7 characters: "Bearer ")
        jwt = authHeader.substring(7);
        
        // 4. Extract the username from the token using your toolbox
        username = jwtUtil.extractUsername(jwt);

        // 5. If we have a username and the user isn't already authenticated in this session...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Find the user in the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the mathematical signature and expiration date
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                
                // Create the official Spring Security authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Officially log the user in for this specific request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 6. Pass the request along to the next filter or the final controller
        filterChain.doFilter(request, response);
    }
}