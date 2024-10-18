package com.example.ecommerce.service;

import com.example.ecommerce.entity.Usuario;
import com.example.ecommerce.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
@Service

public class JwtUserDetailsService implements  UserDetailsService{
    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user details for username: " + username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities("ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
