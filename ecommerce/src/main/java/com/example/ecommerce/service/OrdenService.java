package com.example.ecommerce.service;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.InventarioRepository;
import com.example.ecommerce.repository.OrdenRepository;
import com.example.ecommerce.repository.ProductoRepository;
import com.example.ecommerce.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private static final Logger log = LoggerFactory.getLogger(OrdenService.class);

    @Transactional
    public OrdenDTO crearOrden(OrdenDTO ordenDTO) {
        log.info("Iniciando creación de orden: {}", ordenDTO);

        try {
            Usuario usuario = usuarioRepository.findById(ordenDTO.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + ordenDTO.getUsuarioId()));
            log.info("Usuario encontrado: {}", usuario.getId());

            Orden orden = new Orden();
            orden.setUsuario(usuario);
            orden.setFecha(LocalDateTime.now());
            orden.setEstado(Orden.EstadoOrden.PENDIENTE);

            List<DetalleOrden> detalles = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (DetalleOrdenDTO detalleDTO : ordenDTO.getDetalles()) {
                log.info("Procesando detalle de orden: {}", detalleDTO);

                Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + detalleDTO.getProductoId()));
                log.info("Producto encontrado: {}", producto.getId());

                Inventario inventario = inventarioRepository.findByProductoId(producto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para el producto con id: " + producto.getId()));
                log.info("Inventario encontrado: {}", inventario.getId());

                if (inventario.getCantidad() < detalleDTO.getCantidad()) {
                    throw new ResourceNotFoundException.InsufficientStockException("Stock insuficiente para el producto: " + producto.getNombre());
                }

                DetalleOrden detalle = new DetalleOrden();
                detalle.setOrden(orden);
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalles.add(detalle);

                total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));

                inventario.setCantidad(inventario.getCantidad() - detalleDTO.getCantidad());
                inventarioRepository.save(inventario);
            }

            orden.setDetalles(detalles);
            orden.setTotal(total);

            BigDecimal descuento = calcularDescuento(orden);
            orden.setDescuento(descuento);
            orden.setTotal(total.subtract(descuento));

            orden = ordenRepository.save(orden);
            log.info("Orden creada exitosamente: {}", orden.getId());

            return convertToDTO(orden);
        } catch (Exception e) {
            log.error("Error al crear la orden", e);
            throw e;
        }
    }
    @Transactional(readOnly = true)
    public OrdenDTO obtenerOrdenPorId(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return convertToDTO(orden);
    }

    @Transactional(readOnly = true)
    public Page<OrdenDTO> listarOrdenes(Pageable pageable) {
        return ordenRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> obtenerOrdenesPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> obtenerOrdenesPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ordenRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> getTopVentas() {
        return ordenRepository.findAll().stream()
                .sorted((o1, o2) -> o2.getTotal().compareTo(o1.getTotal()))
                .limit(5)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrdenDTO actualizarEstadoOrden(Long id, String nuevoEstado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setEstado(Orden.EstadoOrden.valueOf(nuevoEstado));
        orden = ordenRepository.save(orden);
        return convertToDTO(orden);
    }

    @Transactional
    public void cancelarOrden(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setEstado(Orden.EstadoOrden.CANCELADA);
        ordenRepository.save(orden);
        // Aquí podrías añadir lógica para revertir el inventario si es necesario
    }

    @Transactional(readOnly = true)
    public EstadisticasOrdenDTO obtenerEstadisticasOrdenes() {
        List<Orden> todasLasOrdenes = ordenRepository.findAll();

        long totalOrdenes = todasLasOrdenes.size();
        BigDecimal ventasTotales = todasLasOrdenes.stream()
                .map(Orden::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ventasPromedio = totalOrdenes > 0
                ? ventasTotales.divide(BigDecimal.valueOf(totalOrdenes), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Map<String, Long> ordenesPorEstado = todasLasOrdenes.stream()
                .collect(Collectors.groupingBy(orden -> orden.getEstado().name(), Collectors.counting()));

        List<EstadisticasOrdenDTO.ProductoTopVentasDTO> productosTopVentas = obtenerProductosTopVentas(todasLasOrdenes);
        List<EstadisticasOrdenDTO.ClienteTopComprasDTO> clientesTopCompras = obtenerClientesTopCompras(todasLasOrdenes);

        EstadisticasOrdenDTO estadisticas = new EstadisticasOrdenDTO();
        estadisticas.setTotalOrdenes(totalOrdenes);
        estadisticas.setVentasTotales(ventasTotales);
        estadisticas.setVentasPromedio(ventasPromedio);
        estadisticas.setOrdenesPorEstado(ordenesPorEstado);
        estadisticas.setProductosTopVentas(productosTopVentas);
        estadisticas.setClientesTopCompras(clientesTopCompras);

        return estadisticas;
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

    public boolean esClienteFrecuente(Long usuarioId) {
        List<Object[]> topClientes = ordenRepository.findTopCustomers();
        return topClientes.stream()
                .limit(5)
                .anyMatch(cliente -> cliente[0].equals(usuarioId));
    }
    public List<ClienteFrecuenteDTO> obtenerClientesFrecuentes() {
        List<Object[]> topClientes = ordenRepository.findTopUsers();
        return topClientes.stream()
                .limit(5)
                .map(cliente -> new ClienteFrecuenteDTO(
                        (Long) cliente[0],
                        (String) cliente[1],
                        (Long) cliente[2],
                        (BigDecimal) cliente[3]))
                .collect(Collectors.toList());
    }


    private void actualizarInventario(List<DetalleOrden> detalles) {
        for (DetalleOrden detalle : detalles) {
            Inventario inventario = inventarioRepository.findByProductoId(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario: " + detalle.getProducto().getId()));

            if (inventario.getCantidad() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + detalle.getProducto().getNombre());
            }

            inventario.setCantidad(inventario.getCantidad() - detalle.getCantidad());
            inventarioRepository.save(inventario);
        }
    }

    private List<EstadisticasOrdenDTO.ProductoTopVentasDTO> obtenerProductosTopVentas(List<Orden> ordenes) {
        return ordenes.stream()
                .flatMap(orden -> orden.getDetalles().stream())
                .collect(Collectors.groupingBy(
                        detalle -> detalle.getProducto().getId(),
                        Collectors.summarizingLong(DetalleOrden::getCantidad)
                ))
                .entrySet().stream()
                .map(entry -> {
                    EstadisticasOrdenDTO.ProductoTopVentasDTO dto = new EstadisticasOrdenDTO.ProductoTopVentasDTO();
                    Producto producto = productoRepository.findById(entry.getKey()).orElseThrow();
                    dto.setProductoId(entry.getKey());
                    dto.setNombreProducto(producto.getNombre());
                    dto.setCantidadVendida(entry.getValue().getSum());
                    dto.setTotalVentas(producto.getPrecio().multiply(BigDecimal.valueOf(entry.getValue().getSum())));
                    return dto;
                })
                .sorted((a, b) -> b.getTotalVentas().compareTo(a.getTotalVentas()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<EstadisticasOrdenDTO.ClienteTopComprasDTO> obtenerClientesTopCompras(List<Orden> ordenes) {
        return ordenes.stream()
                .collect(Collectors.groupingBy(
                        orden -> orden.getUsuario().getId(),
                        Collectors.summarizingDouble(orden -> orden.getTotal().doubleValue())
                ))
                .entrySet().stream()
                .map(entry -> {
                    EstadisticasOrdenDTO.ClienteTopComprasDTO dto = new EstadisticasOrdenDTO.ClienteTopComprasDTO();
                    dto.setUsuarioId(entry.getKey());
                    dto.setNombreUsuario(usuarioRepository.findById(entry.getKey()).orElseThrow().getUsername());
                    dto.setCantidadOrdenes(entry.getValue().getCount());
                    dto.setTotalCompras(BigDecimal.valueOf(entry.getValue().getSum()));
                    return dto;
                })
                .sorted((a, b) -> b.getTotalCompras().compareTo(a.getTotalCompras()))
                .limit(5)
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
                orden.getDescuento(),
                orden.getEstado().name()
        );
    }
    public List<ProductoDTO> getProductosActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private ProductoDTO convertToDTO(Producto producto) {
        return  new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.isActivo()
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
