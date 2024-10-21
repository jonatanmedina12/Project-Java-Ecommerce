package com.example.ecommerce.dto;

import java.math.BigDecimal;

public class ClienteFrecuenteDTO {
    private Long usuarioId;
    private String nombre;
    private Long cantidadOrdenes;
    private BigDecimal totalGastado;


    public ClienteFrecuenteDTO(Long usuarioId, String nombre, Long cantidadOrdenes, BigDecimal totalGastado) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.cantidadOrdenes = cantidadOrdenes;
        this.totalGastado = totalGastado;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCantidadOrdenes() {
        return cantidadOrdenes;
    }

    public void setCantidadOrdenes(Long cantidadOrdenes) {
        this.cantidadOrdenes = cantidadOrdenes;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }
}
