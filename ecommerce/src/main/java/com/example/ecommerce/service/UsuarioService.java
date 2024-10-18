package com.example.ecommerce.service;

import com.example.ecommerce.dto.UsuarioDTO;
import com.example.ecommerce.entity.Rol;
import com.example.ecommerce.entity.Usuario;
import com.example.ecommerce.repository.OrdenRepository;
import com.example.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
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

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(usuario);
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
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setActivo(usuario.isActivo());
        dto.setRol(usuario.getRol().name());
        // No incluimos la contraseña por razones de seguridad
        return dto;
    }
}
