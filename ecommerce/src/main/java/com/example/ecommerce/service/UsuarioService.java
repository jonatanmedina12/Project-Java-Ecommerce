package com.example.ecommerce.service;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.entity.Rol;
import com.example.ecommerce.entity.Usuario;
import com.example.ecommerce.repository.OrdenRepository;
import com.example.ecommerce.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ecommerce.exception.UserValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getUsername() == null || usuarioDTO.getUsername().trim().isEmpty()) {
            throw new UserValidationException("El nombre de usuario no puede estar vacío", "username");
        }
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            throw new UserValidationException("El email no puede estar vacío", "email");
        }
        if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().trim().isEmpty()) {
            throw new UserValidationException("La contraseña no puede estar vacía", "password");
        }
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new UserValidationException("El nombre de usuario ya existe", "username");
        }
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new UserValidationException("El email ya está registrado", "email");
        }

        LocalDateTime fechaHoraActual = LocalDateTime.now();

        Usuario usuario = new Usuario();
        usuario.setCreated_on(fechaHoraActual);
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setActivo(true);
        usuario.setRol(Rol.USUARIO);

        usuario = usuarioRepository.save(usuario);
        return convertToDTO(usuario);
    }

    @Transactional
    public void inciarSesion(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        logger.info("Validando para el usuario1: " + usuario.getUsername());

        usuario.setLast_login(fechaHoraActual);
        usuario.setActivoLogin(true);
        usuarioRepository.save(usuario);
    }
    @Transactional
    public void cerrarSesion(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        logger.info("Validando para el usuario2: " + usuario.getUsername());

        usuario.setLast_login(fechaHoraActual);
        usuario.setActivoLogin(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarContrasena(ResetPasswordDTO resetPasswordDTO) {

        Usuario usuario = usuarioRepository.findByEmail(resetPasswordDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        logger.info("Validando para el usuario: " + usuario.getUsername());

        usuario.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        usuario.setUpdate_on(fechaHoraActual);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public boolean validarEmail(ValidarEmail validarEmail) {
        return usuarioRepository.existsByEmail(validarEmail.getEmail());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(usuario);
    }
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(usuario);
    }

    @Transactional
    public Usuario obtenerUsuarioEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));
        return usuario;
    }
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getUsername().equals(usuarioDTO.getUsername()) &&
                usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nuevo nombre de usuario ya existe");
        }

        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El nuevo email ya está registrado");
        }

        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        usuario.setActivo(usuarioDTO.isActivo());

        usuario = usuarioRepository.save(usuario);
        return convertToDTO(usuario);
    }
    @Transactional
    public boolean actualizarUsuarioUsername(UsuarioUpdateDto usuarioDTO, String fileName) {
        Usuario usuario = usuarioRepository.findByUsername(usuarioDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isUpdated = false;

        if (usuarioDTO.getUsername() != null && !usuarioDTO.getUsername().equals(usuario.getUsername())) {
            usuario.setUsername(usuarioDTO.getUsername());
            isUpdated = true;
        }

        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(usuario.getEmail())) {
            usuario.setEmail(usuarioDTO.getEmail());
            isUpdated = true;
        }

        if (fileName != null) {
            usuario.setFoto("/uploads/" + fileName);
            isUpdated = true;
        }

        if (isUpdated) {
            usuario.setUpdate_on(LocalDateTime.now());
            usuarioRepository.save(usuario);
        }

        return isUpdated;
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> getClientesFrecuentes() {
        List<Object[]> topClientes = ordenRepository.findTopCustomers();
        return topClientes.stream()
                .limit(5)
                .map(result -> {
                    Long userId = (Long) result[0];
                    Long orderCount = (Long) result[1];
                    Usuario usuario = usuarioRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    UsuarioDTO dto = convertToDTO(usuario);
                    dto.setTotalOrdenes(orderCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.isActivo(),
                usuario.getRol().name(),
                usuario.getFoto()
        );
    }
}
