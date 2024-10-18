package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Inventario findByProductoId(Long productoId);

}
