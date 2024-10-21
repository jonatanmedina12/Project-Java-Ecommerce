package com.example.ecommerce.service;

import com.example.ecommerce.controller.AuthController;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.LoginResponse;
import com.example.ecommerce.dto.ResetPasswordDTO;
import com.example.ecommerce.dto.ValidarEmail;
import com.example.ecommerce.entity.Usuario;
import com.example.ecommerce.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private UsuarioService usuarioService;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioEmail(loginRequest.getEmail());
            logger.info("validando informacion: " + usuario.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username=usuario.getUsername();
            logger.info("validando acaa 222222: " + username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String jwt = jwtUtils.generateToken(userDetails);

            return new LoginResponse(jwt,usuario.getId(),usuario.getUsername(),usuario.getEmail(),usuario.getRol(),usuario.isActivoLogin() );
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid email or password", e) {
            };
        }
    }
    public String inicioSesion(String username) {
        try {
            usuarioService.inciarSesion(username);



            return "Ok";
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid inicio", e) {
            };
        }
    }

    public String refreshToken(String refreshToken, String Username) {
        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(Username);

            return jwtUtils.generateToken(userDetails);



        } catch (Exception e) {
            throw new RuntimeException("Error refreshing token: " + e.getMessage());
        }
    }

    public boolean logout(String Username) {
        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(Username);

            if (userDetails != null) {
                String username = userDetails.getUsername();
                usuarioService.cerrarSesion(username);
                return true;
            }else {

                throw new RuntimeException("Error logout");
            }



        } catch (Exception e) {
            throw new RuntimeException("Error logout: " + e.getMessage());
        }

    }
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        try {

            if (resetPasswordDTO != null) {

                usuarioService.cambiarContrasena(resetPasswordDTO);

            }else {
                throw new RuntimeException("Error reset password");
            }


        } catch (Exception e) {
            throw new RuntimeException("Error logout: " + e.getMessage());
        }
    }
    public boolean validarEmail(ValidarEmail validarEmail) {
        try {

            if (validarEmail != null) {

               return usuarioService.validarEmail(validarEmail);

            }else {
                throw new RuntimeException("Error reset password");
            }


        } catch (Exception e) {
            throw new RuntimeException("Error logout: " + e.getMessage());
        }
    }
}
