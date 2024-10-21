package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ClienteFrecuenteDTO;
import com.example.ecommerce.dto.OrdenDTO;
import com.example.ecommerce.dto.ProductoDTO;
import com.example.ecommerce.dto.ProductoVentaDTO;
import com.example.ecommerce.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {
    @Autowired
    private OrdenService reportService;

    @GetMapping("/productos-activos")
    public ResponseEntity<List<ProductoDTO>> getProductosActivos() {
        List<ProductoDTO> productosActivos = reportService.getProductosActivos();
        return ResponseEntity.ok(productosActivos);
    }

    @GetMapping("/top-ventas")
    public ResponseEntity<List<OrdenDTO>> getTopVentas() {
        List<OrdenDTO> topVentas = reportService.getTopVentas();
        return ResponseEntity.ok(topVentas);
    }
    @GetMapping("/clientes-frecuentes")
    public ResponseEntity<List<ClienteFrecuenteDTO>> getClientesFrecuentes() {
        List<ClienteFrecuenteDTO> clientesFrecuentes = reportService.obtenerClientesFrecuentes();
        return ResponseEntity.ok(clientesFrecuentes);
    }

}
