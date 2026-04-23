package com.example.backend_cafedronel.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;
import com.example.backend_cafedronel.model.Inventario;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final List<Inventario> inventario = new ArrayList<>();
    private int nextId = 1;

    public InventarioServiceImpl() {
        Inventario i1 = new Inventario();
        i1.setId(nextId++); i1.setNombreInsumo("Granos de café");
        i1.setCantidad(50); i1.setUnidad("kg");
        i1.setStockMinimo(10); i1.setPrecioUnitario(25.0f);
        i1.setProveedor("Distribuidora Café Peru");
        i1.setFechaActualizacion(LocalDateTime.now());
        inventario.add(i1);

        Inventario i2 = new Inventario();
        i2.setId(nextId++); i2.setNombreInsumo("Leche entera");
        i2.setCantidad(30); i2.setUnidad("litros");
        i2.setStockMinimo(5); i2.setPrecioUnitario(4.5f);
        i2.setProveedor("Lácteos del Norte");
        i2.setFechaActualizacion(LocalDateTime.now());
        inventario.add(i2);
    }

    @Override
    public List<Inventario> listar() { return new ArrayList<>(inventario); }

    @Override
    public Map<String, Object> listarConRespuesta() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", new ArrayList<>(inventario));
        return resp;
    }

    @Override
    public Map<String, Object> agregar(Inventario item) {
        Map<String, Object> resp = new HashMap<>();
        try {
            item.setId(nextId++);
            item.setFechaActualizacion(LocalDateTime.now());
            inventario.add(item);
            resp.put("success", true);
            resp.put("message", "Producto agregado correctamente.");
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", "Error al agregar producto: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public Map<String, Object> editar(Integer id, Inventario actualizado) {
        Map<String, Object> resp = new HashMap<>();
        for (Inventario item : inventario) {
            if (item.getId().equals(id)) {
                item.setNombreInsumo(actualizado.getNombreInsumo());
                item.setCantidad(actualizado.getCantidad());
                item.setUnidad(actualizado.getUnidad());
                item.setStockMinimo(actualizado.getStockMinimo());
                item.setPrecioUnitario(actualizado.getPrecioUnitario());
                item.setProveedor(actualizado.getProveedor());
                item.setFechaActualizacion(LocalDateTime.now());
                resp.put("success", true);
                resp.put("message", "Producto actualizado correctamente.");
                return resp;
            }
        }
        resp.put("success", false);
        resp.put("message", "Producto no encontrado.");
        return resp;
    }

    @Override
    public Map<String, Object> eliminar(Integer id) {
        Map<String, Object> resp = new HashMap<>();
        if (inventario.removeIf(i -> i.getId().equals(id))) {
            resp.put("success", true);
            resp.put("message", "Producto eliminado correctamente.");
        } else {
            resp.put("success", false);
            resp.put("message", "Producto no encontrado.");
        }
        return resp;
    }

    @Override
    public Map<String, Object> restarStock(Integer id, int cantidad) {
        Map<String, Object> resp = new HashMap<>();
        for (Inventario item : inventario) {
            if (item.getId().equals(id)) {
                item.setCantidad(Math.max(item.getCantidad() - cantidad, 0));
                item.setFechaActualizacion(LocalDateTime.now());
                resp.put("success", true);
                resp.put("message", "Stock actualizado correctamente.");
                resp.put("data", item);
                return resp;
            }
        }
        resp.put("success", false);
        resp.put("message", "Producto no encontrado.");
        return resp;
    }
}
