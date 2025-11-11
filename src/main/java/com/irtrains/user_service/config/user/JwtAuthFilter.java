package com.irtrains.user_service.config.user;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.irtrains.user_service.service.*;

import java.io.IOException;
import java.util.Set;

@Component // Must be a Spring bean
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTUtility jwtService;
    private final UserDetailsService userService;
    private final UserDetailsService adminService;

    // Constructor injection
    public JwtAuthFilter(JWTUtility jwtService, @Qualifier("userService") UserDetailsService userService,
                         @Qualifier("adminService") UserDetailsService adminService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.adminService = adminService;
    }

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/account/admin/login",
            "/api/account/admin/register",
            "/api/account/user/login",
            "/api/account/user/register",
            "/api/trains"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return PUBLIC_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Determine which UserDetailsService to use based on the request path
            UserDetailsService userDetailsService;
            if (request.getRequestURI().contains("/api/admin")) {
                userDetailsService = adminService;
            } else {
                userDetailsService = userService;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(token)) { // You might want to pass userDetails to validateToken
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
