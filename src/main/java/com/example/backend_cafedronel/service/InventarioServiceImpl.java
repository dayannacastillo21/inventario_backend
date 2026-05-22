package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.BusinessException;
import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Inventario;
import com.example.backend_cafedronel.model.Proveedor;
import com.example.backend_cafedronel.repository.InventarioRepository;
import com.example.backend_cafedronel.repository.ProveedorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProveedorRepository proveedorRepository;

    public InventarioServiceImpl(
            InventarioRepository inventarioRepository,
            ProveedorRepository proveedorRepository) {
        this.inventarioRepository = inventarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> listar() {
        return inventarioRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> listarConStockBajo() {
        return inventarioRepository.findConStockBajo();
    }

    @Override
    @Transactional(readOnly = true)
    public Inventario obtenerPorId(Integer id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario", id));
    }

    @Override
    @Transactional
    public Inventario crear(Inventario item) {
        item.setId(null);
        item.setProveedorEntidad(resolverProveedor(item.getProveedor()));
        return inventarioRepository.save(item);
    }

    @Override
    @Transactional
    public Inventario actualizar(Integer id, Inventario actualizado) {
        Inventario item = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario", id));

        item.setNombreInsumo(actualizado.getNombreInsumo());
        item.setCantidad(actualizado.getCantidad());
        item.setUnidad(actualizado.getUnidad());
        item.setStockMinimo(actualizado.getStockMinimo());
        item.setPrecioUnitario(actualizado.getPrecioUnitario());
        item.setProveedorEntidad(resolverProveedor(actualizado.getProveedor()));
        return inventarioRepository.save(item);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!inventarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventario", id);
        }
        inventarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Inventario deducirStock(Integer id, int unidades) {
        if (unidades <= 0) {
            throw new BusinessException("Las unidades a deducir deben ser mayores que cero");
        }
        Inventario item = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario", id));
        item.setCantidad(Math.max(item.getCantidad() - unidades, 0));
        return inventarioRepository.save(item);
    }

    private Proveedor resolverProveedor(String nombre) {
        return proveedorRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", nombre));
    }
}
