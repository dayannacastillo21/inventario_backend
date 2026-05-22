package com.example.backend_cafedronel.repository;

import com.example.backend_cafedronel.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    @Query("select i from Inventario i where i.cantidad <= i.stockMinimo order by i.id asc")
    List<Inventario> findConStockBajo();
}
