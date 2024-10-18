package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductoDTO;
import com.example.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoDTO>> getProductosActivos() {
        return ResponseEntity.ok(productoService.getProductosActivos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam String criterio) {
        return ResponseEntity.ok(productoService.buscarProductos(criterio));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.crearProducto(productoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();

    }
}


