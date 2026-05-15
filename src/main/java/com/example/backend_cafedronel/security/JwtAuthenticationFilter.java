package com.example.backend_cafedronel.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    public JwtAuthenticationFilter(JwtService jwtService) { this.jwtService = jwtService; }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (jwtService.isValid(token)) {
                String email = jwtService.extractUsername(token);
                String role = String.valueOf(jwtService.extractClaims(token).get("role"));
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.replace("ROLE_", "").toUpperCase()));
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null, authorities));
            }
        }
        filterChain.doFilter(request, response);
    }
}
