package pe.edu.uls.tests_productos.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.dto.RequestMantenimiento;
import pe.edu.uls.tests_productos.model.Equipo;
import pe.edu.uls.tests_productos.model.Mantenimiento;
import pe.edu.uls.tests_productos.model.MantenimientoDetalle;
import pe.edu.uls.tests_productos.model.Producto;
import pe.edu.uls.tests_productos.repository.EquipoRepository;
import pe.edu.uls.tests_productos.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.repository.ProductoRepository;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final EquipoRepository equipoRepository;
    private final ProductoRepository productoRepository;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            EquipoRepository equipoRepository,
            ProductoRepository productoRepository) {

        this.mantenimientoRepository = mantenimientoRepository;
        this.equipoRepository = equipoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Mantenimiento registrarMantenimiento(
            RequestMantenimiento request) {

        Equipo equipo = equipoRepository.findById(request.equipoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipo no encontrado con ID: "
                                + request.equipoId()
                ));

        Mantenimiento mantenimiento = new Mantenimiento();

        mantenimiento.setFecha(request.fecha());
        mantenimiento.setTipo(request.tipo());
        mantenimiento.setObservaciones(request.observaciones());
        mantenimiento.setEquipo(equipo);

        if (request.detalles() == null || request.detalles().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El mantenimiento debe tener al menos un detalle"
            );
        }

        for (RequestMantenimiento.DetalleMantenimientoRequest detalleRequest
                : request.detalles()) {

            Producto producto = productoRepository
                    .findById(detalleRequest.productoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Producto no encontrado con ID: "
                                    + detalleRequest.productoId()
                    ));

            MantenimientoDetalle detalle =
                    new MantenimientoDetalle();

            detalle.setCantidad(detalleRequest.cantidad());
            detalle.setDescripcion(detalleRequest.descripcion());
            detalle.setProducto(producto);

            mantenimiento.agregarDetalle(detalle);
        }

        return mantenimientoRepository.save(mantenimiento);
    }

    public Mantenimiento obtenerMantenimiento(int id) {

        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mantenimiento no encontrado con ID: " + id
                ));
    }

    public List<Mantenimiento> listarMantenimientos() {

        return mantenimientoRepository.findAll();
    }
}