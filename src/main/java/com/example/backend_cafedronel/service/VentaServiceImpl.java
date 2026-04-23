package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.BusinessException;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Producto;
import com.example.backend_cafedronel.model.Venta;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private static final ZoneId LIMA_ZONE = ZoneId.of("America/Lima");

    private final ProductoService productoService;
    private final List<Venta> ventas = new ArrayList<>();
    private int nextId = 1;

    public VentaServiceImpl(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostConstruct
    void seedDemoVenta() {
        Producto p1 = productoService.obtenerPorId(1).orElse(null);
        if (p1 == null) {
            return;
        }
        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        Venta v1 = new Venta();
        v1.setId(nextId++);
        v1.setUsuarioId(1);
        v1.setCantidad(2);
        v1.setProducto(copiaProductoParaDocumento(p1));
        v1.setPrecioUnitario(p1.getPrecio());
        v1.setTotal(v1.getCantidad() * p1.getPrecio());
        v1.setEstado("completado");
        v1.setMetodoPago("efectivo");
        v1.setFechaVenta(ahora);
        ventas.add(v1);
    }

    private static Producto copiaProductoParaDocumento(Producto origen) {
        Producto copia = new Producto();
        copia.setId(origen.getId());
        copia.setNombre(origen.getNombre());
        copia.setPrecio(origen.getPrecio());
        copia.setCategoria(origen.getCategoria());
        copia.setDescripcion(origen.getDescripcion());
        copia.setFechaCreacion(Timestamp.from(ZonedDateTime.now(LIMA_ZONE).toInstant()));
        return copia;
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
        if (venta.getProducto() == null || venta.getProducto().getId() == null) {
            throw new BusinessException("Debe indicar el id del producto");
        }
        Producto producto = productoService.obtenerPorId(venta.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", venta.getProducto().getId()));

        venta.setProducto(copiaProductoParaDocumento(producto));
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
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        if (ventaActualizada.getProducto() == null || ventaActualizada.getProducto().getId() == null) {
            throw new BusinessException("Debe indicar el id del producto");
        }
        Producto producto = productoService.obtenerPorId(ventaActualizada.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", ventaActualizada.getProducto().getId()));

        venta.setUsuarioId(ventaActualizada.getUsuarioId());
        venta.setCantidad(ventaActualizada.getCantidad());
        venta.setProducto(copiaProductoParaDocumento(producto));
        venta.setMetodoPago(ventaActualizada.getMetodoPago());
        venta.setEstado(ventaActualizada.getEstado());

        venta.setPrecioUnitario(producto.getPrecio());
        venta.setTotal(venta.getCantidad() * producto.getPrecio());

        return venta;
    }

    @Override
    public void eliminar(Integer id) {
        boolean removed = ventas.removeIf(v -> v.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Venta", id);
        }
    }
}
