package com.example.ecommerce.controller;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.security.JwtUtils;
import com.example.ecommerce.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            authService.inicioSesion(response.getUsername());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        logger.info("Validando token para el usuario: " + refreshTokenDTO.getToken());
        logger.info("Validando token para el usuario2: " + refreshTokenDTO.getUsername());

        if (refreshTokenDTO.getUsername() != null && refreshTokenDTO.getToken() != null) {
            try {
                String newAccessToken = authService.refreshToken(refreshTokenDTO.getToken(), refreshTokenDTO.getUsername());
                return ResponseEntity.ok(newAccessToken);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid2 refresh token");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Authorization header");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDto logoutDto) {
        logger.info("Validando token para el usuario 2 : " +logoutDto.getUsername() );

        if ( logoutDto.getUsername() != null ) {
            try {
                 boolean estado=authService.logout(logoutDto.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body(estado);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid logout");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Authorization header");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        logger.info("password : " +resetPasswordDTO.getUsername() );

        if ( resetPasswordDTO.getUsername() != null ) {
            try {
                authService.resetPassword(resetPasswordDTO);
                return ResponseEntity.ok(true);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid contraseña");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al cambiar la contraseña");
        }
    }
    @PostMapping("/validar-email")
    public ResponseEntity<?> validarEmail(@RequestBody ValidarEmail validarEmail) {

        logger.info("password : " +validarEmail.getEmail() );

        if ( validarEmail.getEmail() != null ) {
            try {
                boolean estado =authService.validarEmail(validarEmail);
                if (estado){
                    return ResponseEntity.ok(estado);

                }else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Correo no encontrado:\t"+estado);
                }

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid correo");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al validar el correo");
        }
    }
}
