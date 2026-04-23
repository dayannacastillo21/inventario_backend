package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.BusinessException;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.DetallePedido;
import com.example.backend_cafedronel.model.Pedido;
import com.example.backend_cafedronel.model.Producto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final ProductoService productoService;
    private final List<Pedido> pedidos = new ArrayList<>();
    private int nextId = 1;
    private int nextDetalleId = 1;

    public PedidoServiceImpl(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostConstruct
    void seedDemoPedido() {
        Producto p1 = productoService.obtenerPorId(1).orElse(null);
        Producto p2 = productoService.obtenerPorId(2).orElse(null);
        if (p1 == null || p2 == null) {
            return;
        }
        Timestamp ahora = new Timestamp(System.currentTimeMillis());
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

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }

    @Override
    public Optional<Pedido> obtenerPorId(Integer id) {
        return pedidos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    private Producto resolverProducto(Integer id) {
        return productoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    @Override
    public Pedido crear(Pedido pedido) {
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new BusinessException("El pedido debe incluir al menos un detalle");
        }

        pedido.setId(nextId++);
        pedido.setEstado(Pedido.EstadoPedido.pendiente);
        pedido.setFecha(new Timestamp(System.currentTimeMillis()));

        double total = 0;
        for (DetallePedido d : pedido.getDetalles()) {
            d.setId(nextDetalleId++);
            d.setPedidoId(pedido.getId());

            if (d.getProducto() == null || d.getProducto().getId() == null) {
                throw new BusinessException("Cada detalle debe referenciar un producto por id");
            }
            Producto producto = resolverProducto(d.getProducto().getId());
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
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        pedido.setCliente(pedidoActualizado.getCliente());
        pedido.setDetalles(pedidoActualizado.getDetalles());

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new BusinessException("El pedido debe incluir al menos un detalle");
        }

        double total = 0;
        for (DetallePedido d : pedido.getDetalles()) {
            d.setId(nextDetalleId++);
            d.setPedidoId(pedido.getId());
            if (d.getProducto() == null || d.getProducto().getId() == null) {
                throw new BusinessException("Cada detalle debe referenciar un producto por id");
            }
            Producto producto = resolverProducto(d.getProducto().getId());
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
        Pedido pedido = pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        if (estado == null) {
            throw new BusinessException("El estado es obligatorio");
        }
        switch (estado.toLowerCase()) {
            case "en_proceso" -> pedido.setEstado(Pedido.EstadoPedido.en_proceso);
            case "completado" -> pedido.setEstado(Pedido.EstadoPedido.completado);
            case "cancelado" -> pedido.setEstado(Pedido.EstadoPedido.cancelado);
            default -> throw new BusinessException("Estado no válido: " + estado);
        }
        return pedido;
    }

    @Override
    public void eliminar(Integer id) {
        boolean removed = pedidos.removeIf(p -> p.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Pedido", id);
        }
    }
}
