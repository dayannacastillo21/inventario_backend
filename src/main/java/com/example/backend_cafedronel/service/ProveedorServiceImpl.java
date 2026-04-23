package com.example.backend_cafedronel.service;

import org.springframework.stereotype.Service;
import java.util.*;
import com.example.backend_cafedronel.model.Proveedor;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final List<Proveedor> proveedores = new ArrayList<>();
    private int nextId = 1;

    public ProveedorServiceImpl() {
        Proveedor pv = new Proveedor();
        pv.setId(nextId++); pv.setNombre("Distribuidora Café Peru");
        pv.setTelefono("987654321"); pv.setDireccion("Av. Lima 123");
        pv.setEmail("contacto@cafeperu.com"); pv.setActivo(true);
        proveedores.add(pv);
    }

    @Override
    public List<Proveedor> listar() { return new ArrayList<>(proveedores); }

    @Override
    public Proveedor crear(Proveedor proveedor) {
        proveedor.setId(nextId++);
        proveedores.add(proveedor);
        return proveedor;
    }

    @Override
    public Proveedor actualizar(Integer id, Proveedor actualizado) {
        for (Proveedor p : proveedores) {
            if (p.getId().equals(id)) {
                p.setNombre(actualizado.getNombre());
                p.setTelefono(actualizado.getTelefono());
                p.setDireccion(actualizado.getDireccion());
                p.setEmail(actualizado.getEmail());
                p.setActivo(actualizado.isActivo());
                return p;
            }
        }
        throw new RuntimeException("Proveedor no encontrado");
    }

    @Override
    public void eliminar(Integer id) {
        if (proveedores.removeIf(p -> p.getId().equals(id)) == false)
            throw new RuntimeException("Proveedor no encontrado");
    }
}
