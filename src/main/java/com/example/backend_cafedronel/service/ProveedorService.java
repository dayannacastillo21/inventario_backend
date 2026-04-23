package com.example.backend_cafedronel.service;

import java.util.List;
import com.example.backend_cafedronel.model.Proveedor;

public interface ProveedorService {

    List<Proveedor> listar();

    Proveedor crear(Proveedor proveedor);

    Proveedor actualizar(Integer id, Proveedor proveedor);

    void eliminar(Integer id);
}