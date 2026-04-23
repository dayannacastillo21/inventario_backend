package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.dto.LoginRequest;
import com.example.backend_cafedronel.dto.LoginResponse;
import com.example.backend_cafedronel.dto.UsuarioRegistroRequest;
import com.example.backend_cafedronel.dto.UsuarioUpdateRequest;
import com.example.backend_cafedronel.exception.DuplicateEmailException;
import com.example.backend_cafedronel.exception.InvalidCredentialsException;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public LoginResponse autenticar(LoginRequest request) {
        Optional<Usuario> found = usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(request.getEmail())
                        && u.getPassword().equals(request.getPassword())
                        && Boolean.TRUE.equals(u.getActivo()))
                .findFirst();

        if (found.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        Usuario u = found.get();
        return new LoginResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol());
    }

    @Override
    public Usuario registrar(UsuarioRegistroRequest request) {
        boolean existe = usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(request.getEmail()));
        if (existe) {
            throw new DuplicateEmailException(request.getEmail());
        }
        Usuario nuevo = new Usuario();
        nuevo.setId(nextId++);
        nuevo.setNombre(request.getNombre());
        nuevo.setEmail(request.getEmail());
        nuevo.setPassword(request.getPassword());
        nuevo.setRol(request.getRol() != null && !request.getRol().isBlank() ? request.getRol() : "usuario");
        nuevo.setActivo(true);
        nuevo.setFechaRegistro(LocalDateTime.now());
        usuarios.add(nuevo);
        return nuevo;
    }

    @Override
    public Usuario actualizar(Integer id, UsuarioUpdateRequest usuarioActualizado) {
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setPassword(usuarioActualizado.getPassword());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setActivo(usuarioActualizado.getActivo());

        return usuario;
    }

    @Override
    public void eliminar(Integer id) {
        if (!usuarios.removeIf(u -> u.getId().equals(id))) {
            throw new ResourceNotFoundException("Usuario", id);
        }
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios);
    }
}
