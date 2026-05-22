package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.model.Inventario;

import java.util.List;

public interface InventarioService {

    List<Inventario> listar();

    List<Inventario> listarConStockBajo();

    Inventario obtenerPorId(Integer id);

    Inventario crear(Inventario item);

    Inventario actualizar(Integer id, Inventario actualizado);

    void eliminar(Integer id);

    Inventario deducirStock(Integer id, int unidades);
}
