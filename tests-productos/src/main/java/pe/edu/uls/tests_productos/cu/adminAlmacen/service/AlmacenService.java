package pe.edu.uls.tests_productos.cu.adminAlmacen.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.uls.tests_productos.domain.entity.Almacen;
import pe.edu.uls.tests_productos.domain.repository.AlmacenRepository;

@Service
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    public Almacen obtenerAlmacen(int id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Almacén no encontrado con ID: " + id));
    }

    public List<Almacen> listarAlmacenes() {
        return almacenRepository.findAll();
    }

    public List<Almacen> buscarPorEstado(Integer estado) {
        return almacenRepository.findByEstado(estado);
    }

    public Almacen registrarAlmacen(Almacen almacen) {
        if (almacenRepository.existsByCodigo(almacen.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un almacén con el código: " + almacen.getCodigo());
        }
        return almacenRepository.save(almacen);
    }
}
