package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.BusinessException;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Proveedor;
import com.example.backend_cafedronel.repository.ProveedorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listar() {
        return proveedorRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor obtenerPorId(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", id));
    }

    @Override
    @Transactional
    public Proveedor crear(Proveedor proveedor) {
        proveedorRepository.findByEmailIgnoreCase(proveedor.getEmail())
                .ifPresent(existing -> {
                    throw new BusinessException("Ya existe un proveedor con el correo " + proveedor.getEmail());
                });
        proveedor.setId(null);
        return proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional
    public Proveedor actualizar(Integer id, Proveedor actualizado) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", id));

        proveedorRepository.findByEmailIgnoreCase(actualizado.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Ya existe un proveedor con el correo " + actualizado.getEmail());
                });

        proveedor.setNombre(actualizado.getNombre());
        proveedor.setTelefono(actualizado.getTelefono());
        proveedor.setDireccion(actualizado.getDireccion());
        proveedor.setEmail(actualizado.getEmail());
        proveedor.setActivo(actualizado.isActivo());
        return proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!proveedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor", id);
        }
        proveedorRepository.deleteById(id);
    }
}
