package com.example.backend_cafedronel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "✅ Backend conectado correctamente a Spring Boot y funcionando en puerto 8081!";
    }
}

//ESTA CLASE ES SOLO PARA COMPROBAR QUE FUNCIONA SPRING BOOT