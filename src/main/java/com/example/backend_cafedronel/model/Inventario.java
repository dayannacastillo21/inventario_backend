package com.example.backend_cafedronel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "inventario")
public class Inventario {
    private static final ZoneId LIMA_ZONE = ZoneId.of("America/Lima");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_insumo", nullable = false, length = 150)
    private String nombreInsumo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 30)
    private String unidad;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id", nullable = false)
    @JsonIgnore
    private Proveedor proveedorEntidad;

    @Transient
    private String proveedor;

    @JsonProperty("fechaActualizacion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void onCreate() {
        if (fechaActualizacion == null) {
            fechaActualizacion = LocalDateTime.now(LIMA_ZONE);
        }
    }

    @PreUpdate
    void onUpdate() {
        fechaActualizacion = LocalDateTime.now(LIMA_ZONE);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Float getPrecioUnitario() {
        return precioUnitario != null ? precioUnitario.floatValue() : null;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario != null ? BigDecimal.valueOf(precioUnitario.doubleValue()) : null;
    }

    @JsonIgnore
    public BigDecimal getPrecioUnitarioValor() { return precioUnitario; }
    public void setPrecioUnitarioValor(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getProveedor() {
        return proveedorEntidad != null ? proveedorEntidad.getNombre() : proveedor;
    }

    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    @JsonIgnore
    public Proveedor getProveedorEntidad() { return proveedorEntidad; }
    public void setProveedorEntidad(Proveedor proveedorEntidad) { this.proveedorEntidad = proveedorEntidad; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
