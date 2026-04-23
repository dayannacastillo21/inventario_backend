package com.example.backend_cafedronel.service;

import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import com.example.backend_cafedronel.model.Pedido;
import com.example.backend_cafedronel.model.Producto;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import com.example.backend_cafedronel.model.DetallePedido;

@Service
public class PedidoServiceImpl implements PedidoService {

    private List<Pedido> pedidos = new ArrayList<>();
    private List<Producto> productos = new ArrayList<>();

    private int nextId = 1;
    private int nextDetalleId = 1;

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }

    @Override
    public Optional<Pedido> obtenerPorId(Integer id) {
        return pedidos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public PedidoServiceImpl() {

        Timestamp ahora = new Timestamp(System.currentTimeMillis());

        Producto p1 = new Producto();
        p1.setId(1);
        p1.setNombre("Café Americano");
        p1.setPrecio(8.0);
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

        Producto p3 = new Producto();
        p3.setId(3);
        p3.setNombre("Croissant");
        p3.setPrecio(6.5);
        p3.setCategoria("comida");
        p3.setDescripcion("Croissant de mantequilla");
        p3.setFechaCreacion(ahora);

        productos.add(p1);
        productos.add(p2);
        productos.add(p3);

        Pedido pedido = new Pedido();
        pedido.setId(nextId++);
        pedido.setCliente("Demo");
        pedido.setEstado(Pedido.EstadoPedido.pendiente);
        pedido.setFecha(ahora);

        DetallePedido d1 = new DetallePedido();
        d1.setId(nextDetalleId++);
        d1.setCantidad(2);
        d1.setPedidoId(pedido.getId());
        d1.setProducto(p1);
        d1.setPrecio(p1.getPrecio());
        d1.setSubtotal(2 * p1.getPrecio());

        DetallePedido d2 = new DetallePedido();
        d2.setId(nextDetalleId++);
        d2.setCantidad(1);
        d2.setPedidoId(pedido.getId());
        d2.setProducto(p2);
        d2.setPrecio(p2.getPrecio());
        d2.setSubtotal(1 * p2.getPrecio());

        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(d1);
        detalles.add(d2);

        pedido.setDetalles(detalles);
        pedido.setTotal(d1.getSubtotal() + d2.getSubtotal());

        pedidos.add(pedido);
    }

    private Producto buscarProducto(Integer id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Pedido crear(Pedido pedido) {

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new RuntimeException("El pedido no tiene detalles");
        }

        pedido.setId(nextId++);
        pedido.setEstado(Pedido.EstadoPedido.pendiente);
        pedido.setFecha(new Timestamp(System.currentTimeMillis()));

        double total = 0;

        for (DetallePedido d : pedido.getDetalles()) {
            d.setId(nextDetalleId++);
            d.setPedidoId(pedido.getId());

            Producto producto = buscarProducto(d.getProducto().getId());

            if (producto == null) {
                throw new RuntimeException("Producto no existe");
            }

            d.setProducto(producto);
            d.setPrecio(producto.getPrecio());

            double subtotal = d.getCantidad() * producto.getPrecio();
            d.setSubtotal(subtotal);

            total += subtotal;
        }

        pedido.setTotal(total);

        pedidos.add(pedido);
        return pedido;
    }

    @Override
    public Pedido actualizar(Integer id, Pedido pedidoActualizado) {

        Pedido pedido = pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (pedido == null) return null;

        pedido.setCliente(pedidoActualizado.getCliente());
        pedido.setDetalles(pedidoActualizado.getDetalles());

        double total = 0;

        for (DetallePedido d : pedido.getDetalles()) {
            d.setId(nextDetalleId++);
            d.setPedidoId(pedido.getId());

            Producto producto = buscarProducto(d.getProducto().getId());

            if (producto == null) {
                throw new RuntimeException("Producto no existe");
            }

            d.setProducto(producto);
            d.setPrecio(producto.getPrecio());

            double subtotal = d.getCantidad() * producto.getPrecio();
            d.setSubtotal(subtotal);

            total += subtotal;
        }

        pedido.setTotal(total);

        return pedido;
    }

    @Override
    public Pedido actualizarEstado(Integer id, String estado) {
        Pedido pedido = pedidos.stream().filter(p -> p.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        switch (estado.toLowerCase()) {
            case "en_proceso":
                pedido.setEstado(Pedido.EstadoPedido.en_proceso);
                break;
            case "completado":
                pedido.setEstado(Pedido.EstadoPedido.completado);
                break;
            case "cancelado":
                pedido.setEstado(Pedido.EstadoPedido.cancelado);
                break;
            default:
                throw new RuntimeException("Estado no válido");
        }

        return pedido;
    }

    @Override
    public void eliminar(Integer id) {
        pedidos.removeIf(p -> p.getId().equals(id));
    }
}