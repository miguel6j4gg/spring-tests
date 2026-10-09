package pe.edu.uls.tests_productos.cu.adminAlmacen.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.uls.tests_productos.cu.adminAlmacen.service.AlmacenService;
import pe.edu.uls.tests_productos.domain.entity.Almacen;

@RestController
@RequestMapping("/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping("/{id}")
    public Almacen obtenerAlmacen(@PathVariable int id) {
        return almacenService.obtenerAlmacen(id);
    }

    @GetMapping
    public List<Almacen> listarAlmacenes() {
        return almacenService.listarAlmacenes();
    }

    @PostMapping
    public Almacen registrarAlmacen(@RequestBody Almacen almacen) {
        return almacenService.registrarAlmacen(almacen);
    }
}
