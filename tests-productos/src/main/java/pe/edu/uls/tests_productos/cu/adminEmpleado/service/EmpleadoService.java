package pe.edu.uls.tests_productos.cu.adminEmpleado.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.uls.tests_productos.domain.entity.Empleado;
import pe.edu.uls.tests_productos.domain.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Empleado obtenerEmpleado(int id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id));
    }

    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    public List<Empleado> buscarPorEstado(Integer estado) {
        return empleadoRepository.findByEstado(estado);
    }

    public Empleado registrarEmpleado(Empleado empleado) {
        if (empleadoRepository.existsByDni(empleado.getDni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un empleado con el DNI: " + empleado.getDni());
        }
        return empleadoRepository.save(empleado);
    }
}
