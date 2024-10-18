package com.example.ecommerce.controller;

import com.example.ecommerce.dto.InventarioDTO;
import com.example.ecommerce.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioDTO>> obtenerTodoElInventario() {
        List<InventarioDTO> inventario = inventarioService.obtenerTodoElInventario();
        return new ResponseEntity<>(inventario, HttpStatus.OK);
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<InventarioDTO> obtenerInventarioPorProductoId(@PathVariable Long productoId) {
        InventarioDTO inventario = inventarioService.obtenerInventarioPorProductoId(productoId);
        return new ResponseEntity<>(inventario, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InventarioDTO> crearInventario(@RequestBody InventarioDTO inventarioDTO) {
        InventarioDTO nuevoInventario = inventarioService.crearInventario(inventarioDTO);
        return new ResponseEntity<>(nuevoInventario, HttpStatus.CREATED);
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<InventarioDTO> actualizarInventario(@PathVariable Long productoId, @RequestBody InventarioDTO inventarioDTO) {
        InventarioDTO inventarioActualizado = inventarioService.actualizarInventario(productoId, inventarioDTO);
        return new ResponseEntity<>(inventarioActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long productoId) {
        inventarioService.eliminarInventario(productoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{productoId}/actualizar-stock")
    public ResponseEntity<String> actualizarStock(@PathVariable Long productoId, @RequestParam int cantidad) {
        boolean actualizado = inventarioService.actualizarStock(productoId, cantidad);
        if (actualizado) {
            return new ResponseEntity<>("Stock actualizado con éxito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo actualizar el stock", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productoId}/verificar-disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(@PathVariable Long productoId, @RequestParam int cantidadRequerida) {
        boolean disponible = inventarioService.verificarDisponibilidad(productoId, cantidadRequerida);
        return new ResponseEntity<>(disponible, HttpStatus.OK);
    }
}
