package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.model.DetallePedido;
import com.example.backend_cafedronel.model.Pedido;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final PedidoService pedidoService;

    public DetallePedidoServiceImpl(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Override
    public List<DetallePedido> obtenerPorPedido(Integer pedidoId) {
        Optional<Pedido> pedido = pedidoService.obtenerPorId(pedidoId);
        if (pedido.isPresent() && pedido.get().getDetalles() != null) {
            return pedido.get().getDetalles();
        }
        return new ArrayList<>();
    }
}
