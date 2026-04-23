package com.example.backend_cafedronel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend_cafedronel.model.Venta;
import com.example.backend_cafedronel.service.VentaService;
import java.util.List;

@RestController
@RequestMapping("/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> getAll() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @PostMapping
    public ResponseEntity<Venta> create(@RequestBody Venta venta) {
        return ResponseEntity.ok(ventaService.crear(venta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizar(@PathVariable Integer id, @RequestBody Venta ventaActualizada) {
        Venta actualizada = ventaService.actualizar(id, ventaActualizada);

        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}