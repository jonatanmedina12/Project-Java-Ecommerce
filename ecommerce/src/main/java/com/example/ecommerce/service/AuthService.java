package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.LoginResponse;
import com.example.ecommerce.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service

public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwt = jwtUtils.generateToken(userDetails);

            return new LoginResponse(jwt, "Login successful");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid username or password", e) {
            };
        }
    }
}
