package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductoDTO;
import com.example.ecommerce.entity.Producto;
import com.example.ecommerce.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<ProductoDTO> getProductosActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> buscarProductos(String criterio) {
        return productoRepository.findByNombreContainingOrDescripcionContaining(criterio, criterio).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = convertToEntity(productoDTO);
        producto = productoRepository.save(producto);
        return convertToDTO(producto);
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setActivo(productoDTO.isActivo());

        producto = productoRepository.save(producto);
        return convertToDTO(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    private ProductoDTO convertToDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.isActivo()
        );
    }

    private Producto convertToEntity(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setActivo(productoDTO.isActivo());
        return producto;
    }
}