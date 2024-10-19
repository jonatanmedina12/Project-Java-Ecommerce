package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenDTO {

    private Long id;
    private Long usuarioId;
    private List<DetalleOrdenDTO> detalles;
    private LocalDateTime fecha;
    private BigDecimal total;
    private BigDecimal descuento;
    private String estado;

    // Constructor
    public OrdenDTO(Long id, Long usuarioId, List<DetalleOrdenDTO> detalles, LocalDateTime fecha, BigDecimal total, BigDecimal descuento, String estado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.detalles = detalles;
        this.fecha = fecha;
        this.total = total;
        this.descuento = descuento;
        this.estado = estado;
    }

    public OrdenDTO() {

    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public List<DetalleOrdenDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenDTO> detalles) { this.detalles = detalles; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
