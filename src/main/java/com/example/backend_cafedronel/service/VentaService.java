package com.example.backend_cafedronel.service;

import java.util.List;
import com.example.backend_cafedronel.model.Venta;

public interface VentaService {

    List<Venta> listar();

    Venta crear(Venta venta);

    Venta actualizar(Integer id, Venta venta);

    void eliminar(Integer id);
}