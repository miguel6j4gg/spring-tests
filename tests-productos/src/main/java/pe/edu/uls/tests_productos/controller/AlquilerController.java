package pe.edu.uls.tests_productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.uls.tests_productos.dto.RequestAlquiler;
import pe.edu.uls.tests_productos.dto.ResponseAlquiler;
import pe.edu.uls.tests_productos.model.Alquiler;
import pe.edu.uls.tests_productos.model.AlquilerDetalle;
import pe.edu.uls.tests_productos.services.AlquilerService;

@RestController
@RequestMapping("/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(
            AlquilerService alquilerService) {

        this.alquilerService = alquilerService;
    }

    @PostMapping
    public ResponseAlquiler registrarAlquiler(
            @RequestBody RequestAlquiler request) {

        Alquiler alquiler =
                alquilerService.registrarAlquiler(request);

        return convertirResponse(alquiler);
    }

    @GetMapping("/{id}")
    public ResponseAlquiler obtenerAlquiler(
            @PathVariable int id) {

        Alquiler alquiler =
                alquilerService.obtenerAlquiler(id);

        return convertirResponse(alquiler);
    }

    @GetMapping
    public List<ResponseAlquiler> listarAlquileres() {

        return alquilerService.listarAlquileres()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    private ResponseAlquiler convertirResponse(
            Alquiler alquiler) {

        List<ResponseAlquiler.DetalleAlquilerResponse> detalles =
                alquiler.getDetalles()
                        .stream()
                        .map(this::convertirDetalle)
                        .toList();

        return new ResponseAlquiler(
                alquiler.getId(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getEstado(),
                alquiler.getCliente().getId(),
                alquiler.getCliente().getNombre(),
                detalles
        );
    }

    private ResponseAlquiler.DetalleAlquilerResponse convertirDetalle(
            AlquilerDetalle detalle) {

        return new ResponseAlquiler.DetalleAlquilerResponse(
                detalle.getId(),

                detalle.getEquipo().getId(),
                detalle.getEquipo().getCodigo(),
                detalle.getEquipo().getNombre(),

                detalle.getProducto().getId(),
                detalle.getProducto().getCodigo(),
                detalle.getProducto().getNombre(),

                detalle.getCantidad(),
                detalle.getPrecioUnitario(),

                detalle.getHorometroSalida(),
                detalle.getHorometroRetorno(),

                detalle.getFechaSalida(),
                detalle.getFechaRetorno()
        );
    }
}