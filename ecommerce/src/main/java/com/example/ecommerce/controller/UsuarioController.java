package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UsuarioDTO;
import com.example.ecommerce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.crearUsuario(usuarioDTO));
    }

    @GetMapping("/clientes-frecuentes")
    public ResponseEntity<List<UsuarioDTO>> getClientesFrecuentes() {
        return ResponseEntity.ok(usuarioService.getClientesFrecuentes());
    }

}
