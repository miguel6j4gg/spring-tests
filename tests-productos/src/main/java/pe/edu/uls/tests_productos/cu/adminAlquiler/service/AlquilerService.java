package pe.edu.uls.tests_productos.cu.adminAlquiler.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@Service
public class AlquilerService {

        private final AlquilerRepository alquilerRepository;
        private final ClienteRepository clienteRepository;
        private final EquipoRepository equipoRepository;
        private final ProductoRepository productoRepository;

        public AlquilerService(
                        AlquilerRepository alquilerRepository,
                        ClienteRepository clienteRepository,
                        EquipoRepository equipoRepository,
                        ProductoRepository productoRepository) {

                this.alquilerRepository = alquilerRepository;
                this.clienteRepository = clienteRepository;
                this.equipoRepository = equipoRepository;
                this.productoRepository = productoRepository;
        }

        @Transactional
        public Alquiler registrarAlquiler(
                        RequestAlquiler request) {

                Cliente cliente = clienteRepository.findById(request.clienteId())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Cliente no encontrado con ID: "
                                                                + request.clienteId()));

                if (request.detalles() == null || request.detalles().isEmpty()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "El alquiler debe tener al menos un detalle");
                }

                Alquiler alquiler = new Alquiler();

                alquiler.setFechaInicio(request.fechaInicio());
                alquiler.setFechaFin(request.fechaFin());
                alquiler.setEstado(request.estado());
                alquiler.setCliente(cliente);

                for (RequestAlquiler.DetalleAlquilerRequest detalleRequest : request.detalles()) {

                        Equipo equipo = equipoRepository
                                        .findById(detalleRequest.equipoId())
                                        .orElseThrow(() -> new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Equipo no encontrado con ID: "
                                                                        + detalleRequest.equipoId()));

                        Producto producto = productoRepository
                                        .findById(detalleRequest.productoId())
                                        .orElseThrow(() -> new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Producto no encontrado con ID: "
                                                                        + detalleRequest.productoId()));

                        AlquilerDetalle detalle = new AlquilerDetalle();

                        detalle.setEquipo(equipo);
                        detalle.setProducto(producto);
                        detalle.setCantidad(detalleRequest.cantidad());
                        detalle.setPrecioUnitario(
                                        detalleRequest.precioUnitario());
                        detalle.setHorometroSalida(
                                        detalleRequest.horometroSalida());
                        detalle.setHorometroRetorno(
                                        detalleRequest.horometroRetorno());
                        detalle.setFechaSalida(
                                        detalleRequest.fechaSalida());
                        detalle.setFechaRetorno(
                                        detalleRequest.fechaRetorno());

                        alquiler.agregarDetalle(detalle);
                }

                return alquilerRepository.save(alquiler);
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