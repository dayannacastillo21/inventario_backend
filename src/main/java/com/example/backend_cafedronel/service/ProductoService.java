package com.example.backend_cafedronel.service;

import java.util.List;
import java.util.Optional;
import com.example.backend_cafedronel.model.Producto;

public interface ProductoService {

    List<Producto> listar();

    List<Producto> porCategoria(String categoria);

    Producto crear(Producto producto);

    Optional<Producto> obtenerPorId(Integer id);

    Producto actualizar(Integer id, Producto productoActualizado);

    void eliminar(Integer id);
}