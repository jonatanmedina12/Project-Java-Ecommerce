package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UsernameDto;
import com.example.ecommerce.dto.UsuarioDTO;
import com.example.ecommerce.dto.UsuarioUpdateDto;
import com.example.ecommerce.exception.UserValidationException;
import com.example.ecommerce.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (UserValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("field", e.getField());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error inesperado al crear el usuario");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/actualizarUsuario")
    public ResponseEntity<?> actualizarUsuario(@ModelAttribute UsuarioUpdateDto usuarioDTO) {
        try {
            String fileName = null;
            if (usuarioDTO.getPhoto() != null && !usuarioDTO.getPhoto().isEmpty()) {
                fileName = saveFile(usuarioDTO.getPhoto());
            }

            boolean updated = usuarioService.actualizarUsuarioUsername(usuarioDTO, fileName);
            if (updated) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.ok("No se realizaron cambios en el usuario");
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Error al procesar la imagen"+e, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }
    @PostMapping("/usuario")
    public ResponseEntity<UsuarioDTO> obtenerUsuario( @RequestBody UsernameDto username) {
        try {
            logger.info("dos: " + username.getUsername());

            UsuarioDTO usuario = usuarioService.obtenerUsuarioUsername(username.getUsername());
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/clientes-frecuentes")
    public ResponseEntity<List<UsuarioDTO>> obtenerClientesFrecuentes() {
        List<UsuarioDTO> clientesFrecuentes = usuarioService.getClientesFrecuentes();
        return ResponseEntity.ok(clientesFrecuentes);
    }

}
