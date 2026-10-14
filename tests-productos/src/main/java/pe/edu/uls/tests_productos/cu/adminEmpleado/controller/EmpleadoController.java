package pe.edu.uls.tests_productos.cu.adminEmpleado.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.uls.tests_productos.cu.adminEmpleado.service.EmpleadoService;
import pe.edu.uls.tests_productos.domain.entity.Empleado;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/{id}")
    public Empleado obtenerEmpleado(@PathVariable int id) {
        return empleadoService.obtenerEmpleado(id);
    }

    @GetMapping
    public List<Empleado> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

    @GetMapping("/estado/{estado}")
    public List<Empleado> buscarPorEstado(@PathVariable Integer estado) {
        return empleadoService.buscarPorEstado(estado);
    }

    @PostMapping
    public Empleado registrarEmpleado(@RequestBody Empleado empleado) {
        return empleadoService.registrarEmpleado(empleado);
    }
}
