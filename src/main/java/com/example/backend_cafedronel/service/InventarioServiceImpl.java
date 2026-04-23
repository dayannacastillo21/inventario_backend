package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.BusinessException;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Inventario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private static final ZoneId LIMA_ZONE = ZoneId.of("America/Lima");
    private final List<Inventario> inventario = new ArrayList<>();
    private int nextId = 1;

    public InventarioServiceImpl() {
        Inventario i1 = new Inventario();
        i1.setId(nextId++);
        i1.setNombreInsumo("Granos de café");
        i1.setCantidad(50);
        i1.setUnidad("kg");
        i1.setStockMinimo(10);
        i1.setPrecioUnitario(25.0f);
        i1.setProveedor("Distribuidora Café Peru");
        i1.setFechaActualizacion(LocalDateTime.now(LIMA_ZONE));
        inventario.add(i1);

        Inventario i2 = new Inventario();
        i2.setId(nextId++);
        i2.setNombreInsumo("Leche entera");
        i2.setCantidad(30);
        i2.setUnidad("litros");
        i2.setStockMinimo(5);
        i2.setPrecioUnitario(4.5f);
        i2.setProveedor("Lácteos del Norte");
        i2.setFechaActualizacion(LocalDateTime.now(LIMA_ZONE));
        inventario.add(i2);
    }

    @Override
    public List<Inventario> listar() {
        return new ArrayList<>(inventario);
    }

    @Override
    public Inventario crear(Inventario item) {
        item.setId(nextId++);
        item.setFechaActualizacion(LocalDateTime.now(LIMA_ZONE));
        inventario.add(item);
        return item;
    }

    @Override
    public Inventario actualizar(Integer id, Inventario actualizado) {
        for (Inventario item : inventario) {
            if (item.getId().equals(id)) {
                item.setNombreInsumo(actualizado.getNombreInsumo());
                item.setCantidad(actualizado.getCantidad());
                item.setUnidad(actualizado.getUnidad());
                item.setStockMinimo(actualizado.getStockMinimo());
                item.setPrecioUnitario(actualizado.getPrecioUnitario());
                item.setProveedor(actualizado.getProveedor());
                item.setFechaActualizacion(LocalDateTime.now(LIMA_ZONE));
                return item;
            }
        }
        throw new ResourceNotFoundException("Inventario", id);
    }

    @Override
    public void eliminar(Integer id) {
        boolean removed = inventario.removeIf(i -> i.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Inventario", id);
        }
    }

    @Override
    public Inventario deducirStock(Integer id, int unidades) {
        if (unidades <= 0) {
            throw new BusinessException("Las unidades a deducir deben ser mayores que cero");
        }
        for (Inventario item : inventario) {
            if (item.getId().equals(id)) {
                item.setCantidad(Math.max(item.getCantidad() - unidades, 0));
                item.setFechaActualizacion(LocalDateTime.now(LIMA_ZONE));
                return item;
            }
        }
        throw new ResourceNotFoundException("Inventario", id);
    }
}
