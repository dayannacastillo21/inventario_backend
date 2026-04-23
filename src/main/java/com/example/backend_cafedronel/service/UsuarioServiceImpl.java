package com.example.backend_cafedronel.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;
import com.example.backend_cafedronel.model.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>();
    private int nextId = 1;

    public UsuarioServiceImpl() {
        Usuario admin = new Usuario();
        admin.setId(nextId++);
        admin.setNombre("Admin");
        admin.setEmail("admin@cafedronel.com");
        admin.setPassword("admin123");
        admin.setRol("admin");
        admin.setActivo(true);
        admin.setFechaRegistro(LocalDateTime.now());
        usuarios.add(admin);
    }

    @Override
    public Map<String, Object> login(Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Map<String, Object> response = new HashMap<>();

        Optional<Usuario> found = usuarios.stream()
            .filter(u -> u.getEmail().equals(email)
                    && u.getPassword().equals(password)
                    && Boolean.TRUE.equals(u.getActivo()))
            .findFirst();

        if (found.isPresent()) {
            Usuario u = found.get();
            response.put("success", true);
            response.put("user_id", u.getId());
            response.put("user_name", u.getNombre());
            response.put("email", u.getEmail());
            response.put("role", u.getRol());
        } else {
            response.put("success", false);
            response.put("message", "Credenciales incorrectas o usuario inactivo.");
        }

        return response;
    }

    @Override
    public Map<String, Object> registrar(Usuario nuevoUsuario) {
        Map<String, Object> response = new HashMap<>();
        boolean existe = usuarios.stream().anyMatch(u -> u.getEmail().equals(nuevoUsuario.getEmail()));

        if (existe) {
            response.put("success", false);
            response.put("message", "El email ya está registrado.");
            return response;
        }

        nuevoUsuario.setId(nextId++);
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setFechaRegistro(LocalDateTime.now());
        usuarios.add(nuevoUsuario);

        response.put("success", true);
        response.put("message", "Usuario registrado exitosamente.");
        return response;
    }

    @Override
    public Usuario actualizar(Integer id, Usuario usuarioActualizado) {
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (usuario == null) return null;

        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setPassword(usuarioActualizado.getPassword());
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setActivo(usuarioActualizado.getActivo());

        return usuario;
    }

    @Override
    public void eliminar(Integer id) {
        usuarios.removeIf(u -> u.getId().equals(id));
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios);
    }
}