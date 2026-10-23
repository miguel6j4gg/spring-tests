package pe.edu.uls.tests_productos.cu.adminAlquiler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestActualizarAlquiler;
import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.cu.adminAlquiler.response.DetalleEquiposProductosResponse;
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
    public Alquiler registrarAlquilerConLock(RequestAlquiler request) {
        return procesarRegistroAlquiler(request, true);
    }

    @Transactional
    public Alquiler registrarAlquilerSinLock(RequestAlquiler request) {
        return procesarRegistroAlquiler(request, false);
    }

    @Transactional
    public Alquiler registrarAlquiler(RequestAlquiler request) {
        return registrarAlquilerConLock(request);
    }

    private Alquiler procesarRegistroAlquiler(RequestAlquiler request, boolean usarBloqueo) {
        // Validacion de fechas: fin no puede ser anterior a inicio
        if (request.fechaInicio() != null && request.fechaFin() != null && request.fechaFin().isBefore(request.fechaInicio())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        Entidad entidad = entidadRepository.findById(request.entidadId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Entidad / Cliente no encontrado con ID: "
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

            // Se decide si usar lock o no
            Equipo equipo = usarBloqueo
                    ? equipoRepository.findByIdWithLock(detalleRequest.equipoId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Equipo no encontrado con ID: " + detalleRequest.equipoId()))
                    : equipoRepository.findByIdSinLock(detalleRequest.equipoId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Equipo no encontrado con ID: " + detalleRequest.equipoId()));

            // Comprobacion de disponibilidad: estado 1 = Disponible
            if (equipo.getEstado() != null && equipo.getEstado() != 1) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "El equipo '" + equipo.getNombre() + "' (Código: " + equipo.getCodigo()
                                + ") no se encuentra disponible para alquiler. Estado actual: " + equipo.getEstado());
            }

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            equipo.setEstado(2); // 2 = Alquilado
            equipoRepository.save(equipo);

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

                if (producto.getStock() <= 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Cantidad de productos insuficiente. El producto '" + producto.getNombre()
                                    + "' no tiene stock disponible (Stock: " + producto.getStock() + ")");
                }

                detalleProductos.add(detalleProducto(producto, item.precio()));
            }
            return detalleProductos;
        }

        if (detalleRequest.productoIds() != null && !detalleRequest.productoIds().isEmpty()) {
            for (Integer pId : detalleRequest.productoIds()) {
                Producto producto = productoRepository.findById(pId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Producto no encontrado con ID: " + pId));

                if (producto.getStock() <= 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Cantidad de productos insuficiente. El producto '" + producto.getNombre()
                                    + "' no tiene stock disponible (Stock: " + producto.getStock() + ")");
                }

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

    @Transactional
    public Alquiler actualizarAlquiler(int id, RequestActualizarAlquiler request) {
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Alquiler no encontrado con ID: " + id));

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El cuerpo de la solicitud no puede ser nulo");
        }

        boolean camposActualizados = false;

        if (request.fechaFin() != null) {
            if (alquiler.getFechaInicio() != null && request.fechaFin().isBefore(alquiler.getFechaInicio())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La fecha de fin no puede ser anterior a la fecha de inicio");
            }
            alquiler.setFechaFin(request.fechaFin());
            camposActualizados = true;
        }

        if (request.estado() != null) {
            alquiler.setEstado(request.estado());
            camposActualizados = true;

            // Si el estado pasa a 2 (Finalizado), liberamos los equipos del alquiler a estado 1 (Disponible)
            if (request.estado() == 2 && alquiler.getDetalles() != null) {
                for (AlquilerDetalle d : alquiler.getDetalles()) {
                    Equipo eq = d.getEquipo();
                    if (eq != null) {
                        eq.setEstado(1);
                        equipoRepository.save(eq);
                    }
                }
            }
        }

        if (request.estadoGarantia() != null) {
            alquiler.setEstadoGarantia(request.estadoGarantia());
            camposActualizados = true;
        }

        if (request.fechaDevolucionGarantia() != null) {
            alquiler.setFechaDevolucionGarantia(request.fechaDevolucionGarantia());
            camposActualizados = true;
        }

        if (request.observacionesGarantia() != null) {
            alquiler.setObservacionesGarantia(request.observacionesGarantia());
            camposActualizados = true;
        }

        if (request.fechaRetorno() != null || request.horometroRetorno() != null) {
            if (request.fechaRetorno() != null && alquiler.getFechaInicio() != null 
                    && request.fechaRetorno().toLocalDate().isBefore(alquiler.getFechaInicio())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Fecha de retorno inválida: no puede ser anterior a la fecha de inicio del alquiler");
            }

            if (alquiler.getDetalles() != null) {
                for (AlquilerDetalle d : alquiler.getDetalles()) {
                    if (request.fechaRetorno() != null) {
                        d.setFechaRetorno(request.fechaRetorno());
                    }
                    if (request.horometroRetorno() != null) {
                        d.setHorometroRetorno(request.horometroRetorno());
                    }
                }
            }
            camposActualizados = true;
        }

        if (!camposActualizados) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe proporcionar al menos un campo válido para actualizar");
        }

        return alquilerRepository.save(alquiler);
    }

    public List<Alquiler> buscarPorEntidadYEquipo(int entidadId, int equipoId) {
        if (!entidadRepository.existsById(entidadId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente / Entidad no encontrado con ID: " + entidadId);
        }
        if (!equipoRepository.existsById(equipoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado con ID: " + equipoId);
        }

        List<Alquiler> resultados = alquilerRepository.findAlquileresPorEntidadYEquipo(entidadId, equipoId);
        if (resultados.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron alquileres para la entidad ID " + entidadId + " y equipo ID " + equipoId);
        }
        return resultados;
    }

    public List<Alquiler> buscarPorModeloEquipo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El parámetro 'modelo' no puede estar vacío");
        }

        List<Alquiler> resultados = alquilerRepository.findAlquileresPorModeloEquipo(modelo.trim());
        if (resultados.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron alquileres para el modelo de equipo: " + modelo);
        }
        return resultados;
    }

    @Transactional(readOnly = true)
    public DetalleEquiposProductosResponse obtenerDetalleEquiposProductos(int id) {
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Alquiler no encontrado con ID: " + id));

        List<DetalleEquiposProductosResponse.ItemDetalleResponse> items = alquiler.getDetalles() != null
                ? alquiler.getDetalles().stream().map(d -> {
                    List<DetalleEquiposProductosResponse.ProductoAsociadoResponse> prods = d.getProductos() != null
                            ? d.getProductos().stream().map(p -> new DetalleEquiposProductosResponse.ProductoAsociadoResponse(
                                    p.getProducto().getId(),
                                    p.getProducto().getCodigo(),
                                    p.getProducto().getNombre(),
                                    p.getPrecio()
                            )).toList()
                            : List.of();

                    return new DetalleEquiposProductosResponse.ItemDetalleResponse(
                            d.getId(),
                            d.getEquipo() != null ? d.getEquipo().getId() : 0,
                            d.getEquipo() != null ? d.getEquipo().getCodigo() : "",
                            d.getEquipo() != null ? d.getEquipo().getNombre() : "",
                            d.getEquipo() != null ? d.getEquipo().getModelo() : "",
                            d.getHorometroSalida(),
                            d.getHorometroRetorno(),
                            prods
                    );
                }).toList()
                : List.of();

        return new DetalleEquiposProductosResponse(alquiler.getId(), items);
    }
}