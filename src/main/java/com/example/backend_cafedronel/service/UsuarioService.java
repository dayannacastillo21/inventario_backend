package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.model.Usuario;
import java.util.List;
import java.util.Map;

public interface UsuarioService {
    List<Usuario> listar();
    Map<String, Object> login(Map<String, String> request);
    Map<String, Object> registrar(Usuario nuevoUsuario);
    Usuario actualizar(Integer id, Usuario usuario);
    void eliminar(Integer id);
}