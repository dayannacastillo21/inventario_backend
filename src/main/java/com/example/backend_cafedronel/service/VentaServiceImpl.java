package com.example.backend_cafedronel.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.sql.Timestamp;
import com.example.backend_cafedronel.model.Venta;
import com.example.backend_cafedronel.model.Producto;
import jakarta.annotation.PostConstruct;

@Service
public class VentaServiceImpl implements VentaService {

    private final List<Venta> ventas = new ArrayList<>();
    private final List<Producto> productos = new ArrayList<>();
    private int nextId = 1;

    @PostConstruct
    public void init() {
        Timestamp ahora = new Timestamp(System.currentTimeMillis());

        Producto p1 = new Producto();
        p1.setId(1);
        p1.setNombre("Café Americano");
        p1.setPrecio(25.0);
        p1.setCategoria("bebidas");
        p1.setDescripcion("Café negro clásico");
        p1.setFechaCreacion(ahora);

        Producto p2 = new Producto();
        p2.setId(2);
        p2.setNombre("Cappuccino");
        p2.setPrecio(12.0);
        p2.setCategoria("bebidas");
        p2.setDescripcion("Café con leche espumada");
        p2.setFechaCreacion(ahora);

        productos.add(p1);
        productos.add(p2);

        Venta v1 = new Venta();
        v1.setId(nextId++);
        v1.setUsuarioId(1);
        v1.setCantidad(2);
        v1.setProducto(p1);
        v1.setPrecioUnitario(p1.getPrecio());
        v1.setTotal(v1.getCantidad() * p1.getPrecio());
        v1.setEstado("completado");
        v1.setMetodoPago("efectivo");
        v1.setFechaVenta(ahora);

        ventas.add(v1);
    }

    private Producto buscarProducto(Integer id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Venta> listar() {
        return new ArrayList<>(ventas);
    }

    @Override
    public Venta crear(Venta venta) {
        venta.setId(nextId++);

        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(new Timestamp(System.currentTimeMillis()));
        }

        Producto producto = buscarProducto(venta.getProducto().getId());

        if (producto == null) {
            throw new RuntimeException("Producto no existe");
        }

        venta.setProducto(producto);
        venta.setPrecioUnitario(producto.getPrecio());
        venta.setTotal(venta.getCantidad() * producto.getPrecio());

        ventas.add(venta);
        return venta;
    }

    @Override
    public Venta actualizar(Integer id, Venta ventaActualizada) {

        Venta venta = ventas.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (venta == null) return null;

        Producto producto = buscarProducto(ventaActualizada.getProducto().getId());

        if (producto == null) {
            throw new RuntimeException("Producto no existe");
        }

        venta.setUsuarioId(ventaActualizada.getUsuarioId());
        venta.setCantidad(ventaActualizada.getCantidad());
        venta.setProducto(producto);
        venta.setMetodoPago(ventaActualizada.getMetodoPago());
        venta.setEstado(ventaActualizada.getEstado());

        venta.setPrecioUnitario(producto.getPrecio());
        venta.setTotal(venta.getCantidad() * producto.getPrecio());

        return venta;
    }

    @Override
    public void eliminar(Integer id) {
        ventas.removeIf(v -> v.getId().equals(id));
    }
}