package com.example.backend_cafedronel.controller;

import com.example.backend_cafedronel.dto.ProductoRequest;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Producto;
import com.example.backend_cafedronel.service.ProductoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> porCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.porCategoria(categoria));
    }

    @GetMapping("/busqueda/precio-minimo")
    public ResponseEntity<List<Producto>> porPrecioMinimo(@RequestParam @Positive Double min) {
        return ResponseEntity.ok(productoService.porPrecioMinimo(min));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoRequest request) {
        Producto creado = productoService.crear(toEntity(request));
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @Valid @RequestBody ProductoRequest request) {
        Producto actualizado = productoService.actualizar(id, toEntity(request));
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private static Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());
        producto.setDescripcion(request.getDescripcion());
        return producto;
    }
}
