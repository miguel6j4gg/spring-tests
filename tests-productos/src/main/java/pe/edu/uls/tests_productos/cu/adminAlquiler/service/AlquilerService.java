package pe.edu.uls.tests_productos.cu.adminAlquiler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalleProducto;
import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.EntidadRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@Service
public class AlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final EntidadRepository entidadRepository;
    private final EquipoRepository equipoRepository;
    private final ProductoRepository productoRepository;

    public AlquilerService(
            AlquilerRepository alquilerRepository,
            EntidadRepository entidadRepository,
            EquipoRepository equipoRepository,
            ProductoRepository productoRepository) {

        this.alquilerRepository = alquilerRepository;
        this.entidadRepository = entidadRepository;
        this.equipoRepository = equipoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Alquiler registrarAlquiler(
            RequestAlquiler request) {

        Entidad entidad = entidadRepository.findById(request.entidadId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Entidad no encontrada con ID: "
                                + request.entidadId()));

        if (request.detalles() == null || request.detalles().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El alquiler debe tener al menos un detalle");
        }

        Alquiler alquiler = new Alquiler();

        alquiler.setFechaInicio(request.fechaInicio());
        alquiler.setFechaFin(request.fechaFin());
        alquiler.setEstado(request.estado());
        alquiler.setEntidad(entidad);
        alquiler.setMontoGarantia(request.montoGarantia());
        alquiler.setTipoGarantia(request.tipoGarantia());
        alquiler.setEstadoGarantia(request.estadoGarantia());
        alquiler.setFechaDevolucionGarantia(request.fechaDevolucionGarantia());
        alquiler.setObservacionesGarantia(request.observacionesGarantia());

        for (RequestAlquiler.DetalleAlquilerRequest detalleRequest : request.detalles()) {

            Equipo equipo = equipoRepository
                    .findById(detalleRequest.equipoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Equipo no encontrado con ID: "
                                    + detalleRequest.equipoId()));

            AlquilerDetalle detalle = new AlquilerDetalle();

            detalle.setEquipo(equipo);
            detalle.setCantidad(detalleRequest.cantidad());
            detalle.setPrecioUnitario(detalleRequest.precioUnitario());
            detalle.setHorometroSalida(detalleRequest.horometroSalida());
            detalle.setHorometroRetorno(detalleRequest.horometroRetorno());
            detalle.setFechaSalida(detalleRequest.fechaSalida());
            detalle.setFechaRetornoEstimada(detalleRequest.fechaRetornoEstimada());
            detalle.setFechaRetorno(detalleRequest.fechaRetorno());
            detalle.setLugar(detalleRequest.lugar());
            detalle.setObservaciones(detalleRequest.observaciones());

            for (AlquilerDetalleProducto detalleProducto : crearDetalleProductos(detalleRequest)) {
                detalle.agregarProducto(detalleProducto);
            }

            alquiler.agregarDetalle(detalle);
        }

        return alquilerRepository.save(alquiler);
    }

    private List<AlquilerDetalleProducto> crearDetalleProductos(
            RequestAlquiler.DetalleAlquilerRequest detalleRequest) {

        List<AlquilerDetalleProducto> detalleProductos = new ArrayList<>();

        if (detalleRequest.productos() != null && !detalleRequest.productos().isEmpty()) {
            for (RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest item : detalleRequest.productos()) {
                Producto producto = productoRepository
                        .findById(item.productoId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Producto no encontrado con ID: "
                                        + item.productoId()));
                detalleProductos.add(detalleProducto(producto, item.precio()));
            }
            return detalleProductos;
        }

        if (detalleRequest.productoIds() != null && !detalleRequest.productoIds().isEmpty()) {
            for (Producto producto : productoRepository.findAllById(detalleRequest.productoIds())) {
                detalleProductos.add(detalleProducto(producto, null));
            }
        }
        return detalleProductos;
    }

    private AlquilerDetalleProducto detalleProducto(Producto producto, Double precio) {
        AlquilerDetalleProducto detalleProducto = new AlquilerDetalleProducto();
        detalleProducto.setProducto(producto);
        detalleProducto.setPrecio(precio != null ? precio : producto.getPrecio());
        return detalleProducto;
    }

    public Alquiler obtenerAlquiler(int id) {

        return alquilerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Alquiler no encontrado con ID: " + id));
    }

    public List<Alquiler> listarAlquileres() {

        return alquilerRepository.findAll();
    }
}