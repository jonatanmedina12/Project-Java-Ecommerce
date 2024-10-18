package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface ProductoRepository  extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();
    List<Producto> findByNombreContainingOrDescripcionContaining(String nombre, String descripcion);
}
