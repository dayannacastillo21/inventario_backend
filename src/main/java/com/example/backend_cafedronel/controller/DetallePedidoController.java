package com.example.backend_cafedronel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend_cafedronel.model.DetallePedido;
import com.example.backend_cafedronel.service.DetallePedidoService;
import java.util.List;

@RestController
@RequestMapping("/detalle-pedido")
@CrossOrigin(origins = "*")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<List<DetallePedido>> obtenerDetallesPorPedido(@PathVariable Integer pedidoId) {
        return ResponseEntity.ok(detallePedidoService.obtenerPorPedido(pedidoId));
    }
}
