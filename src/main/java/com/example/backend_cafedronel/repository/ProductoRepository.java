package com.example.backend_cafedronel.repository;

import com.example.backend_cafedronel.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoriaIgnoreCase(String categoria);
    @Query("select p from Producto p where p.precio >= :min")
    List<Producto> buscarConPrecioMinimo(@Param("min") Double min);
}
