package com.example.ecommerce.dto;

public class InventarioDTO {

    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;

    // Constructores, getters y setters
    public InventarioDTO() {}

    public InventarioDTO(Long id, Long productoId, String nombreProducto, Integer cantidad) {
        this.id = id;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
