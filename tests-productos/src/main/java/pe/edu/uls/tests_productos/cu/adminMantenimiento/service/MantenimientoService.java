package pe.edu.uls.tests_productos.cu.adminMantenimiento.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.cu.adminMantenimiento.request.RequestMantenimiento;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.response.ResponseProductoConsumido;
import pe.edu.uls.tests_productos.domain.entity.Empleado;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.EmpleadoRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final EquipoRepository equipoRepository;
    private final ProductoRepository productoRepository;
    private final EmpleadoRepository empleadoRepository;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            EquipoRepository equipoRepository,
            ProductoRepository productoRepository,
            EmpleadoRepository empleadoRepository) {

        this.mantenimientoRepository = mantenimientoRepository;
        this.equipoRepository = equipoRepository;
        this.productoRepository = productoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional
    public Mantenimiento registrarMantenimiento(RequestMantenimiento request) {

        if (request.fechaProgramada() == null || !request.fechaProgramada().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha programada debe ser posterior al día de hoy");
        }

        Equipo equipo = equipoRepository.findById(request.equipoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipo no encontrado con ID: " + request.equipoId()));

        Empleado empleado = null;
        if (request.empleadoId() != null) {
            empleado = empleadoRepository.findById(request.empleadoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Empleado no encontrado con ID: " + request.empleadoId()));
        }

        Mantenimiento mantenimiento = new Mantenimiento();

        mantenimiento.setFechaSolicitud(LocalDate.now());
        mantenimiento.setFechaProgramada(request.fechaProgramada());
        if (request.fechaRealizado() != null) {
            mantenimiento.setFechaRealizado(request.fechaRealizado());
        }
        mantenimiento.setTipo(request.tipo());
        mantenimiento.setObservaciones(request.observaciones());
        mantenimiento.setEquipo(equipo);
        mantenimiento.setEmpleado(empleado);

        if (request.detalles() == null || request.detalles().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El mantenimiento debe tener al menos un detalle");
        }

        for (RequestMantenimiento.DetalleMantenimientoRequest detalleRequest : request.detalles()) {

            List<Producto> productos = List.of();
            if (detalleRequest.productoIds() != null && !detalleRequest.productoIds().isEmpty()) {
                for (Integer productoId : detalleRequest.productoIds()) {
                    if (productoId == null) {
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El producto con ID " + productoId + " no existe");
                    }
                    if (!productoRepository.findById(productoId).isPresent()) {
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El producto con ID " + productoId + " no existe");
                    }
                }
                productos = productoRepository.findAllById(detalleRequest.productoIds());
            }

            MantenimientoDetalle detalle = new MantenimientoDetalle();

            detalle.setCantidad(detalleRequest.cantidad());
            detalle.setDescripcion(detalleRequest.descripcion());
            detalle.setObservaciones(detalleRequest.observaciones());
            detalle.setProductos(productos);

            mantenimiento.agregarDetalle(detalle);
        }

        return mantenimientoRepository.save(mantenimiento);
    }

    public Mantenimiento obtenerMantenimiento(int id) {

        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mantenimiento no encontrado con ID: " + id));
    }

    @Transactional
    public Mantenimiento actualizarFechaRealizado(int id, LocalDate fechaRealizado) {

        if (fechaRealizado == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha realizada no puede ser nula");
        }

        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mantenimiento no encontrado con ID: " + id));

        mantenimiento.setFechaRealizado(fechaRealizado);

        return mantenimientoRepository.save(mantenimiento);
    }

    public List<Mantenimiento> listarMantenimientos() {

        return mantenimientoRepository.findAll();
    }

    public List<Mantenimiento> buscarMantenimientosPorTecnicoYTipo(String dni, String tipo) {

        if (dni == null || dni.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El parámetro 'dni' no puede estar vacío");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El parámetro 'tipo' no puede estar vacío");
        }
        if (!empleadoRepository.existsByDni(dni.trim())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Técnico no encontrado con DNI: " + dni.trim());
        }

        return mantenimientoRepository.findMantenimientosPorTecnicoYTipo(dni.trim(), tipo.trim());
    }

    public List<ResponseProductoConsumido> resumenProductosConsumidosPorEquipo(String codigoEquipo) {

        if (codigoEquipo == null || codigoEquipo.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El parámetro 'codigoEquipo' no puede estar vacío");
        }

        if (!equipoRepository.existsByCodigo(codigoEquipo.trim())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Equipo no encontrado con código: " + codigoEquipo.trim());
        }
        List<Object[]> resultados = mantenimientoRepository
                .resumenProductosConsumidosPorEquipo(codigoEquipo.trim());

        return resultados.stream().map(row -> new ResponseProductoConsumido(
                (String) row[0],
                (String) row[1],
                ((Number) row[2]).longValue())).toList();
    }
}