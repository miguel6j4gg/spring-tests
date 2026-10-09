package pe.edu.uls.tests_productos.cu.adminAlquiler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.repository.AlquilerDetalleRepository;

@RestController
@RequestMapping("/alquiler-detalles")
public class AlquilerDetalleController {

    private final AlquilerDetalleRepository alquilerDetalleRepository;

    public AlquilerDetalleController(AlquilerDetalleRepository alquilerDetalleRepository) {
        this.alquilerDetalleRepository = alquilerDetalleRepository;
    }

    @GetMapping("/{id}")
    public AlquilerDetalle obtenerDetalle(@PathVariable int id) {
        return alquilerDetalleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de alquiler no encontrado con ID: " + id));
    }
}
