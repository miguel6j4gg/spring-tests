package pe.edu.uls.tests_productos.cu.adminProveedor.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.uls.tests_productos.domain.entity.Proveedor;
import pe.edu.uls.tests_productos.domain.repository.ProveedorRepository;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public Proveedor obtenerProveedor(int id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id));
    }

    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public Proveedor registrarProveedor(Proveedor proveedor) {
        if (proveedorRepository.existsByRuc(proveedor.getRuc())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un proveedor con el RUC: " + proveedor.getRuc());
        }
        return proveedorRepository.save(proveedor);
    }
}
