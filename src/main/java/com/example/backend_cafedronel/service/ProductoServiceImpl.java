package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Producto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final List<Producto> productos = new ArrayList<>();
    private int nextId = 1;

    public ProductoServiceImpl() {
        Producto p1 = new Producto();
        p1.setId(nextId++); p1.setNombre("Café Americano"); p1.setPrecio(8.0);
        p1.setCategoria("bebidas"); p1.setDescripcion("Café negro clásico");
        p1.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        productos.add(p1);

        Producto p2 = new Producto();
        p2.setId(nextId++); p2.setNombre("Cappuccino"); p2.setPrecio(12.0);
        p2.setCategoria("bebidas"); p2.setDescripcion("Café con leche espumada");
        p2.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        productos.add(p2);

        Producto p3 = new Producto();
        p3.setId(nextId++); p3.setNombre("Croissant"); p3.setPrecio(6.5);
        p3.setCategoria("comida"); p3.setDescripcion("Croissant de mantequilla");
        p3.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        productos.add(p3);
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    @Override
    public List<Producto> porCategoria(String categoria) {
        List<Producto> result = new ArrayList<>();
        for (Producto p : productos) {
            if (categoria.equalsIgnoreCase(p.getCategoria())) result.add(p);
        }
        return result;
    }

    @Override
    public Producto crear(Producto producto) {
        producto.setId(nextId++);
        producto.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        productos.add(producto);
        return producto;
    }

    @Override
    public Optional<Producto> obtenerPorId(Integer id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public Producto actualizar(Integer id, Producto actualizado) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                p.setNombre(actualizado.getNombre());
                p.setDescripcion(actualizado.getDescripcion());
                p.setPrecio(actualizado.getPrecio());
                p.setCategoria(actualizado.getCategoria());
                return p;
            }
        }
        throw new ResourceNotFoundException("Producto", id);
    }

    @Override
    public void eliminar(Integer id) {
        boolean removed = productos.removeIf(p -> p.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Producto", id);
        }
    }
}
