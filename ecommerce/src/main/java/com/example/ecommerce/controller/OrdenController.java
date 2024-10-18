package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrdenDTO;
import com.example.ecommerce.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @PostMapping
    public ResponseEntity<OrdenDTO> crearOrden(@RequestBody OrdenDTO ordenDTO) {
        return ResponseEntity.ok(ordenService.crearOrden(ordenDTO));
    }

    @GetMapping("/top-ventas")
    public ResponseEntity<List<OrdenDTO>> getTopVentas() {
        return ResponseEntity.ok(ordenService.getTopVentas());
    }


}
