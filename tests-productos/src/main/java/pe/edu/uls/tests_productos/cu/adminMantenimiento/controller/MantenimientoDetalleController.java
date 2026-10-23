package pe.edu.uls.tests_productos.cu.adminMantenimiento.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoDetalleRepository;

@RestController
@RequestMapping("/mantenimiento-detalles")
public class MantenimientoDetalleController {

    private final MantenimientoDetalleRepository mantenimientoDetalleRepository;

    public MantenimientoDetalleController(MantenimientoDetalleRepository mantenimientoDetalleRepository) {
        this.mantenimientoDetalleRepository = mantenimientoDetalleRepository;
    }

    @GetMapping("/{id}")
    public MantenimientoDetalle obtenerDetalle(@PathVariable int id) {
        return mantenimientoDetalleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Detalle de mantenimiento no encontrado con ID: " + id));
    }
}
