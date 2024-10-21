package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrdenRepository  extends JpaRepository<Orden, Long> {

    List<Orden> findByUsuarioId(Long usuarioId);
    List<Orden> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);


    @Query("SELECT o.usuario.id, COUNT(o) as orderCount FROM Orden o GROUP BY o.usuario.id ORDER BY orderCount DESC")
    List<Object[]> findTopCustomers();

    @Query("SELECT u.id, u.username, COUNT(o), SUM(o.total) " +
            "FROM Usuario u INNER JOIN u.ordenes o " +
            "GROUP BY u.id, u.username " +
            "ORDER BY COUNT(o) DESC")
    List<Object[]> findTopUsers();
}
