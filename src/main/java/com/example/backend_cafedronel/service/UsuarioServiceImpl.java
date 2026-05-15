package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.dto.*;
import com.example.backend_cafedronel.exception.*;
import com.example.backend_cafedronel.model.Usuario;
import com.example.backend_cafedronel.repository.UsuarioRepository;
import com.example.backend_cafedronel.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository; private final PasswordEncoder passwordEncoder; private final JwtService jwtService;
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) { this.usuarioRepository = usuarioRepository; this.passwordEncoder = passwordEncoder; this.jwtService = jwtService; }
    public LoginResponse autenticar(LoginRequest request) {
        Usuario u = usuarioRepository.findByEmailIgnoreCase(request.getEmail()).orElseThrow(InvalidCredentialsException::new);
        if(!Boolean.TRUE.equals(u.getActivo()) || !passwordEncoder.matches(request.getPassword(), u.getPassword())) throw new InvalidCredentialsException();
        return new LoginResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol(), jwtService.generateToken(u.getEmail(), u.getRol()));
    }
    @Transactional public Usuario registrar(UsuarioRegistroRequest request) {
        if(usuarioRepository.existsByEmailIgnoreCase(request.getEmail())) throw new DuplicateEmailException(request.getEmail());
        Usuario nuevo = new Usuario(); nuevo.setNombre(request.getNombre()); nuevo.setEmail(request.getEmail()); nuevo.setPassword(passwordEncoder.encode(request.getPassword())); nuevo.setRol(request.getRol()==null?"USER":request.getRol().toUpperCase()); nuevo.setActivo(true);
        return usuarioRepository.save(nuevo);
    }
    @Transactional public Usuario actualizar(Integer id, UsuarioUpdateRequest req) { Usuario u=usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario", id)); u.setNombre(req.getNombre()); u.setEmail(req.getEmail()); if(req.getPassword()!=null&&!req.getPassword().isBlank()) u.setPassword(passwordEncoder.encode(req.getPassword())); u.setRol(req.getRol()); u.setActivo(req.getActivo()); return usuarioRepository.save(u);} 
    @Transactional public void eliminar(Integer id) { if(!usuarioRepository.existsById(id)) throw new ResourceNotFoundException("Usuario", id); usuarioRepository.deleteById(id);} 
    public List<Usuario> listar() { return usuarioRepository.findAll(); }
}
