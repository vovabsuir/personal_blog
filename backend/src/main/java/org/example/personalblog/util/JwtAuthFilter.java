package org.example.personalblog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.personalblog.dto.ErrorResponse;
import org.example.personalblog.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.getTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (token != null) {
            if (jwtService.validateToken(token)) {
                addToSecurityContext(token);
            } else {
                sendErrorResponse(response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void addToSecurityContext(String token) {
        Claims claims = jwtService.getClaims(token);
        String username = claims.getSubject();
        String plainRoles = claims.get("roles", String.class);

        List<GrantedAuthority> roles = Arrays.stream(plainRoles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, roles);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED,
                "Unauthorized Access", "Incorrect JWT");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
