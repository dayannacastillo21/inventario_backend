package com.example.backend_cafedronel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    private String rol; @Column(unique=true, nullable=false) private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) private String password; private String nombre;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime fechaRegistro; private Boolean activo;
    @PrePersist void onCreate(){ if(fechaRegistro==null) fechaRegistro=LocalDateTime.now(); if(activo==null) activo=true; if(rol==null) rol="USER"; }
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getRol() { return rol; } public void setRol(String rol) { this.rol = rol; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; } public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; } public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Boolean getActivo() { return activo; } public void setActivo(Boolean activo) { this.activo = activo; }
}
