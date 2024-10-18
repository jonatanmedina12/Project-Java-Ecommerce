package com.example.ecommerce.service;

import com.example.ecommerce.dto.DetalleOrdenDTO;
import com.example.ecommerce.dto.OrdenDTO;
import com.example.ecommerce.entity.DetalleOrden;
import com.example.ecommerce.entity.Inventario;
import com.example.ecommerce.entity.Orden;
import com.example.ecommerce.entity.Producto;
import com.example.ecommerce.repository.InventarioRepository;
import com.example.ecommerce.repository.OrdenRepository;
import com.example.ecommerce.repository.ProductoRepository;
import com.example.ecommerce.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Transactional
    public OrdenDTO crearOrden(OrdenDTO ordenDTO) {
        Orden orden = new Orden();
        orden.setUsuario(usuarioRepository.findById(ordenDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        orden.setFecha(LocalDateTime.now());

        List<DetalleOrden> detalles = ordenDTO.getDetalles().stream()
                .map(this::convertToDetalleOrden)
                .collect(Collectors.toList());

        orden.setDetalles(detalles);
        Orden finalOrden = orden;
        detalles.forEach(detalle -> detalle.setOrden(finalOrden));

        BigDecimal total = detalles.stream()
                .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orden.setTotal(total);

        // Aplicar descuentos
        BigDecimal descuento = calcularDescuento(orden);
        orden.setDescuento(descuento);
        orden.setTotal(total.subtract(descuento));

        // Actualizar inventario
        actualizarInventario(detalles);

        orden = ordenRepository.save(orden);
        return convertToDTO(orden);
    }

    private BigDecimal calcularDescuento(Orden orden) {
        BigDecimal descuento = BigDecimal.ZERO;

        // Descuento por rango de tiempo (10%)
        LocalDateTime inicio = LocalDateTime.now().minusHours(2); // Por ejemplo, últimas 2 horas
        LocalDateTime fin = LocalDateTime.now();
        if (orden.getFecha().isAfter(inicio) && orden.getFecha().isBefore(fin)) {
            descuento = orden.getTotal().multiply(new BigDecimal("0.10"));
        }

        // Descuento por pedido aleatorio (50%)
        if (Math.random() < 0.1) { // 10% de probabilidad de ser un pedido aleatorio
            descuento = orden.getTotal().multiply(new BigDecimal("0.50"));
        }

        // Descuento por cliente frecuente (5% adicional)
        if (esClienteFrecuente(orden.getUsuario().getId())) {
            descuento = descuento.add(orden.getTotal().multiply(new BigDecimal("0.05")));
        }

        return descuento;
    }

    private boolean esClienteFrecuente(Long usuarioId) {
        List<Object[]> topClientes = ordenRepository.findTopCustomers();
        return topClientes.stream()
                .limit(5)
                .anyMatch(cliente -> cliente[0].equals(usuarioId));
    }

    private void actualizarInventario(List<DetalleOrden> detalles) {
        for (DetalleOrden detalle : detalles) {
            Inventario inventario = inventarioRepository.findByProductoId(detalle.getProducto().getId());
            if (inventario == null) {
                throw new RuntimeException("Producto no encontrado en inventario");
            }
            if (inventario.getCantidad() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }
            inventario.setCantidad(inventario.getCantidad() - detalle.getCantidad());
            inventarioRepository.save(inventario);
        }
    }

    public List<OrdenDTO> getTopVentas() {
        // Implementar lógica para obtener top 5 de ventas
        return ordenRepository.findAll().stream()
                .sorted((o1, o2) -> o2.getTotal().compareTo(o1.getTotal()))
                .limit(5)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DetalleOrden convertToDetalleOrden(DetalleOrdenDTO detalleDTO) {
        DetalleOrden detalle = new DetalleOrden();
        Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        detalle.setProducto(producto);
        detalle.setCantidad(detalleDTO.getCantidad());
        detalle.setPrecioUnitario(producto.getPrecio());
        return detalle;
    }

    private OrdenDTO convertToDTO(Orden orden) {
        return new OrdenDTO(
                orden.getId(),
                orden.getUsuario().getId(),
                orden.getDetalles().stream().map(this::convertToDetalleDTO).collect(Collectors.toList()),
                orden.getFecha(),
                orden.getTotal(),
                orden.getDescuento()
        );
    }

    private DetalleOrdenDTO convertToDetalleDTO(DetalleOrden detalle) {
        return new DetalleOrdenDTO(
                detalle.getId(),
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario()
        );
    }
}
