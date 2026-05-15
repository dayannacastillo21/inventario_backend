package com.example.backend_cafedronel.service;

import com.example.backend_cafedronel.exception.ResourceNotFoundException;
import com.example.backend_cafedronel.model.Producto;
import com.example.backend_cafedronel.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    public ProductoServiceImpl(ProductoRepository productoRepository) { this.productoRepository = productoRepository; }
    public List<Producto> listar() { return productoRepository.findAll(); }
    public List<Producto> porCategoria(String categoria) { return productoRepository.findByCategoriaIgnoreCase(categoria); }
    @Transactional public Producto crear(Producto producto) { return productoRepository.save(producto); }
    public Optional<Producto> obtenerPorId(Integer id) { return productoRepository.findById(id); }
    @Transactional public Producto actualizar(Integer id, Producto actualizado) {
        Producto p = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        p.setNombre(actualizado.getNombre()); p.setDescripcion(actualizado.getDescripcion()); p.setPrecio(actualizado.getPrecio()); p.setCategoria(actualizado.getCategoria());
        return productoRepository.save(p);
    }
    @Transactional public void eliminar(Integer id) { if(!productoRepository.existsById(id)) throw new ResourceNotFoundException("Producto", id); productoRepository.deleteById(id); }
}
