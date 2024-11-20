package com.banquemisr.challenge05.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        if (request.getRequestURI().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isInvalidAuthorizationHeader(authorizationHeader)) {
            log.info("No valid Authorization header found. Proceeding without authentication.");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwt = extractJwtFromHeader(authorizationHeader);
            String email = jwtUtils.extractUsername(jwt);

            if (isAuthenticationRequired(email)) {
                authenticateUser(request, jwt, email);
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.error("JWT validation error: %s", exception.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

    private boolean isInvalidAuthorizationHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean isAuthenticationRequired(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void authenticateUser(HttpServletRequest request, String jwt, String username) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.info("User '%s' successfully authenticated with JWT.", username);
            } else {
                log.error("Invalid JWT token for user '%s'.", username);
            }
        } catch (Exception e) {
            log.error("Error during user authentication: %s", e.getMessage());
        }
    }
}

