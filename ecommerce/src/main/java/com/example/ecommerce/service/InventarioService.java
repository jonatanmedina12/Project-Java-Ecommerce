package com.example.ecommerce.service;

import com.example.ecommerce.dto.InventarioDTO;
import com.example.ecommerce.entity.Inventario;
import com.example.ecommerce.entity.Producto;
import com.example.ecommerce.repository.InventarioRepository;
import com.example.ecommerce.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class InventarioService {
    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<InventarioDTO> obtenerTodoElInventario() {
        return inventarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventarioDTO obtenerInventarioPorProductoId(Long productoId) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId);
        if (inventario == null) {
            throw new RuntimeException("Inventario no encontrado para el producto ID: " + productoId);
        }
        return convertirADTO(inventario);
    }

    @Transactional
    public InventarioDTO crearInventario(InventarioDTO inventarioDTO) {
        Producto producto = productoRepository.findById(inventarioDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + inventarioDTO.getProductoId()));

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(inventarioDTO.getCantidad());

        inventario = inventarioRepository.save(inventario);
        return convertirADTO(inventario);
    }

    @Transactional
    public InventarioDTO actualizarInventario(Long productoId, InventarioDTO inventarioDTO) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId);
        if (inventario == null) {
            throw new RuntimeException("Inventario no encontrado para el producto ID: " + productoId);
        }

        inventario.setCantidad(inventarioDTO.getCantidad());
        inventario = inventarioRepository.save(inventario);
        return convertirADTO(inventario);
    }

    @Transactional
    public void eliminarInventario(Long productoId) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId);
        if (inventario == null) {
            throw new RuntimeException("Inventario no encontrado para el producto ID: " + productoId);
        }
        inventarioRepository.delete(inventario);
    }

    @Transactional
    public boolean actualizarStock(Long productoId, int cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId);
        if (inventario == null) {
            throw new RuntimeException("Inventario no encontrado para el producto ID: " + productoId);
        }

        if (inventario.getCantidad() + cantidad < 0) {
            return false; // No hay suficiente stock
        }

        inventario.setCantidad(inventario.getCantidad() + cantidad);
        inventarioRepository.save(inventario);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean verificarDisponibilidad(Long productoId, int cantidadRequerida) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId);
        if (inventario == null) {
            throw new RuntimeException("Inventario no encontrado para el producto ID: " + productoId);
        }

        return inventario.getCantidad() >= cantidadRequerida;
    }

    private InventarioDTO convertirADTO(Inventario inventario) {
        return new InventarioDTO(
                inventario.getId(),
                inventario.getProducto().getId(),
                inventario.getProducto().getNombre(),
                inventario.getCantidad()
        );
    }
}
