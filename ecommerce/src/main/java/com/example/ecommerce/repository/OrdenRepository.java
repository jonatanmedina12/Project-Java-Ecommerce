package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrdenRepository  extends JpaRepository<Orden, Long> {

    @Query("SELECT o FROM Orden o WHERE o.fecha BETWEEN :startDate AND :endDate")
    List<Orden> findOrdersInDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o.usuario.id, COUNT(o) as orderCount FROM Orden o GROUP BY o.usuario.id ORDER BY orderCount DESC")
    List<Object[]> findTopCustomers();
}
