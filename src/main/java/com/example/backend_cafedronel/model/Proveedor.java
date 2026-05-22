package com.example.backend_cafedronel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String telefono;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private Boolean activo;

    @PrePersist
    void onCreate() {
        if (activo == null) {
            activo = true;
        }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActivo() { return Boolean.TRUE.equals(activo); }
    public void setActivo(boolean activo) { this.activo = activo; }
}
