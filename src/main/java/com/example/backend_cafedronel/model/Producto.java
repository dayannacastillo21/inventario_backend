package com.example.backend_cafedronel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private Double precio;
    private String categoria;
    private String descripcion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private Timestamp fechaCreacion;
    @PrePersist void onCreate(){ if(fechaCreacion==null) fechaCreacion=new Timestamp(System.currentTimeMillis()); }
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; } public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecio() { return precio; } public void setPrecio(Double precio) { this.precio = precio; }
    public String getCategoria() { return categoria; } public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDescripcion() { return descripcion; } public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Timestamp getFechaCreacion() { return fechaCreacion; } public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
