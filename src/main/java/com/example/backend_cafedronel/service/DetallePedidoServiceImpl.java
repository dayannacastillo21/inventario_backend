package com.example.backend_cafedronel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.example.backend_cafedronel.model.DetallePedido;
import com.example.backend_cafedronel.model.Pedido;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {


    
    @Autowired
    private PedidoService pedidoService;

    @Override
    public List<DetallePedido> obtenerPorPedido(Integer pedidoId) {
        Optional<Pedido> pedido = pedidoService.obtenerPorId(pedidoId);
        if (pedido.isPresent() && pedido.get().getDetalles() != null)
            return pedido.get().getDetalles();
        return new ArrayList<>();
    }

    
}
