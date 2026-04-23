package com.example.backend_cafedronel;

import com.example.backend_cafedronel.dto.UsuarioRegistroRequest;
import com.example.backend_cafedronel.exception.DuplicateEmailException;
import com.example.backend_cafedronel.service.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioServiceImplTest {

    @Test
    void registrarUsuario_emailDuplicado_lanzaExcepcion() {
        UsuarioServiceImpl servicio = new UsuarioServiceImpl();

        UsuarioRegistroRequest primero = new UsuarioRegistroRequest();
        primero.setNombre("Uno");
        primero.setEmail("mismo@correo.com");
        primero.setPassword("abcd");
        primero.setRol("usuario");

        UsuarioRegistroRequest duplicado = new UsuarioRegistroRequest();
        duplicado.setNombre("Dos");
        duplicado.setEmail("mismo@correo.com");
        duplicado.setPassword("efgh");
        duplicado.setRol("usuario");

        assertDoesNotThrow(() -> servicio.registrar(primero));
        assertThrows(DuplicateEmailException.class, () -> servicio.registrar(duplicado));
    }
}
