package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class EstadisticasOrdenDTO {
    private long totalOrdenes;
    private BigDecimal ventasTotales;
    private BigDecimal ventasPromedio;
    private Map<String, Long> ordenesPorEstado;
    private List<ProductoTopVentasDTO> productosTopVentas;
    private List<ClienteTopComprasDTO> clientesTopCompras;

    // Getters y setters
    public long getTotalOrdenes() { return totalOrdenes; }
    public void setTotalOrdenes(long totalOrdenes) { this.totalOrdenes = totalOrdenes; }
    public BigDecimal getVentasTotales() { return ventasTotales; }
    public void setVentasTotales(BigDecimal ventasTotales) { this.ventasTotales = ventasTotales; }
    public BigDecimal getVentasPromedio() { return ventasPromedio; }
    public void setVentasPromedio(BigDecimal ventasPromedio) { this.ventasPromedio = ventasPromedio; }
    public Map<String, Long> getOrdenesPorEstado() { return ordenesPorEstado; }
    public void setOrdenesPorEstado(Map<String, Long> ordenesPorEstado) { this.ordenesPorEstado = ordenesPorEstado; }
    public List<ProductoTopVentasDTO> getProductosTopVentas() { return productosTopVentas; }
    public void setProductosTopVentas(List<ProductoTopVentasDTO> productosTopVentas) { this.productosTopVentas = productosTopVentas; }
    public List<ClienteTopComprasDTO> getClientesTopCompras() { return clientesTopCompras; }
    public void setClientesTopCompras(List<ClienteTopComprasDTO> clientesTopCompras) { this.clientesTopCompras = clientesTopCompras; }

    public static class ProductoTopVentasDTO {
        private Long productoId;
        private String nombreProducto;
        private long cantidadVendida;
        private BigDecimal totalVentas;

        // Getters y setters
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        public long getCantidadVendida() { return cantidadVendida; }
        public void setCantidadVendida(long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
        public BigDecimal getTotalVentas() { return totalVentas; }
        public void setTotalVentas(BigDecimal totalVentas) { this.totalVentas = totalVentas; }
    }

    public static class ClienteTopComprasDTO {
        private Long usuarioId;
        private String nombreUsuario;
        private long cantidadOrdenes;
        private BigDecimal totalCompras;

        // Getters y setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
        public long getCantidadOrdenes() { return cantidadOrdenes; }
        public void setCantidadOrdenes(long cantidadOrdenes) { this.cantidadOrdenes = cantidadOrdenes; }
        public BigDecimal getTotalCompras() { return totalCompras; }
        public void setTotalCompras(BigDecimal totalCompras) { this.totalCompras = totalCompras; }
    }
}
