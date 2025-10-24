package org.example.personalblog.service;

import lombok.RequiredArgsConstructor;
import org.example.personalblog.dto.AuthRequest;
import org.example.personalblog.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_LOGIN}")
    private String adminLogin;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    public AuthResponse login(AuthRequest authRequest) {
        if (adminLogin.equals(authRequest.getLogin()) && passwordEncoder.matches(authRequest.getPassword(), adminPassword)) {
            Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");

            return new AuthResponse(jwtService.getAccessToken(adminLogin, authorities));
        } else {
            throw new BadCredentialsException("Invalid login or password");
        }
    }
}
