package com.example.backend_cafedronel.controller;

import com.example.backend_cafedronel.dto.DeduccionStockRequest;
import com.example.backend_cafedronel.dto.InventarioRequest;
import com.example.backend_cafedronel.model.Inventario;
import com.example.backend_cafedronel.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/alertas/stock-bajo")
    public ResponseEntity<List<Inventario>> listarStockBajo() {
        return ResponseEntity.ok(inventarioService.listarConStockBajo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@Valid @RequestBody InventarioRequest request) {
        Inventario creado = inventarioService.crear(toEntity(request));
        return ResponseEntity.created(URI.create("/api/inventario/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable Integer id, @Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.ok(inventarioService.actualizar(id, toEntity(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deducciones")
    public ResponseEntity<Inventario> deducirStock(@PathVariable Integer id, @Valid @RequestBody DeduccionStockRequest request) {
        return ResponseEntity.ok(inventarioService.deducirStock(id, request.getUnidades()));
    }

    private static Inventario toEntity(InventarioRequest request) {
        Inventario item = new Inventario();
        item.setNombreInsumo(request.getNombreInsumo());
        item.setCantidad(request.getCantidad());
        item.setUnidad(request.getUnidad());
        item.setStockMinimo(request.getStockMinimo());
        item.setPrecioUnitario(request.getPrecioUnitario());
        item.setProveedor(request.getProveedor());
        return item;
    }
}
