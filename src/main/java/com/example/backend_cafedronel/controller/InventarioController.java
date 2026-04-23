package com.example.backend_cafedronel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend_cafedronel.model.Inventario;
import com.example.backend_cafedronel.service.InventarioService;
import java.util.*;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listarInventarioSimple() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarInventario() {
        return ResponseEntity.ok(inventarioService.listarConRespuesta());
    }

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregarProducto(@RequestBody Inventario inventario) {
        return ResponseEntity.ok(inventarioService.agregar(inventario));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, Object>> editarProducto(@PathVariable Integer id, @RequestBody Inventario actualizado) {
        return ResponseEntity.ok(inventarioService.editar(id, actualizado));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable Integer id) {
        return ResponseEntity.ok(inventarioService.eliminar(id));
    }

    @PutMapping("/{id}/restar/{cantidad}")
    public ResponseEntity<Map<String, Object>> restarStock(@PathVariable Integer id, @PathVariable int cantidad) {
        return ResponseEntity.ok(inventarioService.restarStock(id, cantidad));
    }
}
