package com.example.backend_cafedronel.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "cliente",
    "estado",
    "total",
    "detalles"
})
public class Pedido {
    private Integer id;
    private String cliente;
    private Double total;
    private EstadoPedido estado;

    private List<DetallePedido> detalles;

    public enum EstadoPedido {
        pendiente, en_proceso, completado, cancelado
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}