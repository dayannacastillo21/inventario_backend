package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.BusinessException;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.DetallePedido;
import com.example.backend_cafedronel.model.Pedido;
import com.example.backend_cafedronel.model.Producto;
import com.example.backend_cafedronel.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final ProductoService productoService;
    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(ProductoService productoService, PedidoRepository pedidoRepository) {
        this.productoService = productoService;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listar() {
        return pedidoRepository.findAllByOrderByIdAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional
    public Pedido crear(Pedido pedido) {
        List<DetallePedido> detallesSolicitados = new ArrayList<>(pedido.getDetalles());

        pedido.setId(null);
        pedido.setEstado(Pedido.EstadoPedido.pendiente);
        pedido.clearDetalles();
        poblarDetallesYTotal(pedido, detallesSolicitados);

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido actualizar(Integer id, Pedido pedidoActualizado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        List<DetallePedido> detallesSolicitados = pedidoActualizado.getDetalles();
        pedido.setCliente(pedidoActualizado.getCliente());
        pedido.clearDetalles();
        poblarDetallesYTotal(pedido, detallesSolicitados);

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido actualizarEstado(Integer id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        if (estado == null || estado.isBlank()) {
            throw new BusinessException("El estado es obligatorio");
        }

        try {
            pedido.setEstado(Pedido.EstadoPedido.valueOf(estado.trim().toLowerCase()));
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Estado no valido: " + estado);
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido", id);
        }
        pedidoRepository.deleteById(id);
    }

    private void poblarDetallesYTotal(Pedido pedido, List<DetallePedido> detallesSolicitados) {
        if (detallesSolicitados == null || detallesSolicitados.isEmpty()) {
            throw new BusinessException("El pedido debe incluir al menos un detalle");
        }

        Set<Integer> productosUsados = new HashSet<>();
        double total = 0;

        for (DetallePedido solicitado : detallesSolicitados) {
            Integer productoId = extraerProductoId(solicitado);
            if (!productosUsados.add(productoId)) {
                throw new BusinessException("El producto " + productoId + " esta duplicado en el pedido");
            }
            if (solicitado.getCantidad() == null || solicitado.getCantidad() <= 0) {
                throw new BusinessException("La cantidad debe ser mayor que cero");
            }

            Producto producto = resolverProducto(productoId);
            double subtotal = solicitado.getCantidad() * producto.getPrecio();

            DetallePedido detalle = new DetallePedido();
            detalle.setCantidad(solicitado.getCantidad());
            detalle.setProducto(producto);
            detalle.setPrecio(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            pedido.addDetalle(detalle);

            total += subtotal;
        }

        pedido.setTotal(total);
    }

    private Integer extraerProductoId(DetallePedido detalle) {
        if (detalle == null || detalle.getProducto() == null || detalle.getProducto().getId() == null) {
            throw new BusinessException("Cada detalle debe referenciar un producto por id");
        }
        return detalle.getProducto().getId();
    }

    private Producto resolverProducto(Integer id) {
        return productoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }
}
