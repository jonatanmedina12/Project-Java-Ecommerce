package com.example.ecommerce.controller;

import com.example.ecommerce.dto.EstadisticasOrdenDTO;
import com.example.ecommerce.dto.OrdenDTO;
import com.example.ecommerce.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @PostMapping
    public ResponseEntity<OrdenDTO> crearOrden(@RequestBody OrdenDTO ordenDTO) {
        try {
            OrdenDTO nuevaOrden = ordenService.crearOrden(ordenDTO);
            return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> obtenerOrden(@PathVariable Long id) {
        try {
            OrdenDTO orden = ordenService.obtenerOrdenPorId(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Page<OrdenDTO>> listarOrdenes(Pageable pageable) {
        Page<OrdenDTO> ordenes = ordenService.listarOrdenes(pageable);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<OrdenDTO>> obtenerOrdenesPorUsuario(@PathVariable Long usuarioId) {
        List<OrdenDTO> ordenes = ordenService.obtenerOrdenesPorUsuario(usuarioId);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<OrdenDTO>> obtenerOrdenesPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<OrdenDTO> ordenes = ordenService.obtenerOrdenesPorFecha(fechaInicio, fechaFin);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/top-ventas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrdenDTO>> getTopVentas() {
        List<OrdenDTO> topVentas = ordenService.getTopVentas();
        return ResponseEntity.ok(topVentas);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenDTO> actualizarEstadoOrden(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            OrdenDTO ordenActualizada = ordenService.actualizarEstadoOrden(id, nuevoEstado);
            return ResponseEntity.ok(ordenActualizada);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelarOrden(@PathVariable Long id) {
        try {
            ordenService.cancelarOrden(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/estadisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstadisticasOrdenDTO> obtenerEstadisticasOrdenes() {
        EstadisticasOrdenDTO estadisticas = ordenService.obtenerEstadisticasOrdenes();
        return ResponseEntity.ok(estadisticas);
    }

}
