package com.example.backend_cafedronel.service;

import java.util.*;
import com.example.backend_cafedronel.model.Inventario;

public interface InventarioService {

    List<Inventario> listar();

    Map<String, Object> listarConRespuesta();

    Map<String, Object> agregar(Inventario inventario);

    Map<String, Object> editar(Integer id, Inventario actualizado);

    Map<String, Object> eliminar(Integer id);

    Map<String, Object> restarStock(Integer id, int cantidad);
}