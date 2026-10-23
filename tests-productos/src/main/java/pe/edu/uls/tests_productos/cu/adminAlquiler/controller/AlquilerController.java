package pe.edu.uls.tests_productos.cu.adminAlquiler.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestActualizarAlquiler;
import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.cu.adminAlquiler.response.DetalleEquiposProductosResponse;
import pe.edu.uls.tests_productos.cu.adminAlquiler.response.ResponseAlquiler;
import pe.edu.uls.tests_productos.cu.adminAlquiler.service.AlquilerService;
import pe.edu.uls.tests_productos.cu.adminProducto.response.ResponseProducto;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.entity.Producto;

@RestController
@RequestMapping("/alquileres")
public class AlquilerController {

        private final AlquilerService alquilerService;

        public AlquilerController(
                        AlquilerService alquilerService) {

                this.alquilerService = alquilerService;
        }

        @PostMapping("/con-lock")
        public ResponseAlquiler registrarAlquilerConLock(
                        @RequestBody RequestAlquiler request) {

                Alquiler alquiler = alquilerService.registrarAlquilerConLock(request);

                return convertirResponse(alquiler);
        }

        @PostMapping("/sin-lock")
        public ResponseAlquiler registrarAlquilerSinLock(
                        @RequestBody RequestAlquiler request) {

                Alquiler alquiler = alquilerService.registrarAlquilerSinLock(request);

                return convertirResponse(alquiler);
        }

        // 1. Operación REST 1: Registrar alquiler
        @PostMapping
        public ResponseAlquiler registrarAlquiler(
                        @RequestBody RequestAlquiler request) {

                Alquiler alquiler = alquilerService.registrarAlquiler(request);

                return convertirResponse(alquiler);
        }

        // 2. Operación REST 2: Obtener alquiler por ID
        @GetMapping("/{id}")
        public ResponseAlquiler obtenerAlquiler(
                        @PathVariable int id) {

                Alquiler alquiler = alquilerService.obtenerAlquiler(id);

                return convertirResponse(alquiler);
        }

        // 3. Operación REST 3: Actualizar fecha de retorno y estado (devolución)
        @org.springframework.web.bind.annotation.RequestMapping(value = "/{id}", method = {
                        org.springframework.web.bind.annotation.RequestMethod.PATCH,
                        org.springframework.web.bind.annotation.RequestMethod.PUT
        })
        public ResponseAlquiler actualizarAlquiler(
                        @PathVariable int id,
                        @RequestBody RequestActualizarAlquiler request) {

                Alquiler alquiler = alquilerService.actualizarAlquiler(id, request);

                return convertirResponse(alquiler);
        }

        // 4. Operación REST 4 (Query 1): Alquileres por modelo de equipo
        @GetMapping("/por-modelo")
        public List<ResponseAlquiler> buscarPorModeloEquipo(
                        @RequestParam String modelo) {

                return alquilerService.buscarPorModeloEquipo(modelo)
                                .stream()
                                .map(this::convertirResponse)
                                .toList();
        }

        // 5. Operación REST 5 (Query 2): Detalle de equipos y productos asociados
        @GetMapping(value = { "/{id}/detalle-equipos-productos", "/detalle-equipos-productos/{id}" })
        public DetalleEquiposProductosResponse obtenerDetalleEquiposProductos(
                        @PathVariable int id) {

                return alquilerService.obtenerDetalleEquiposProductos(id);
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

                List<ResponseAlquiler.DetalleAlquilerResponse> detalles = alquiler.getDetalles() != null
                                ? alquiler.getDetalles().stream().map(this::convertirDetalle).toList()
                                : List.of();

                return new ResponseAlquiler(
                                alquiler.getId(),
                                alquiler.getFechaInicio(),
                                alquiler.getFechaFin(),
                                alquiler.getEstado(),
                                alquiler.getEntidad() != null ? alquiler.getEntidad().getId() : 0,
                                alquiler.getEntidad() != null ? alquiler.getEntidad().getCorreo() : null,
                                alquiler.getMontoGarantia(),
                                alquiler.getTipoGarantia(),
                                alquiler.getEstadoGarantia(),
                                alquiler.getFechaDevolucionGarantia(),
                                alquiler.getObservacionesGarantia(),
                                detalles);
        }

        private ResponseAlquiler.DetalleAlquilerResponse convertirDetalle(
                        AlquilerDetalle detalle) {

                List<ResponseAlquiler.DetalleAlquilerResponse.ProductoAlquiladoResponse> productos = detalle
                                .getProductos() != null
                                                ? detalle.getProductos().stream()
                                                                .map(detalleProducto -> new ResponseAlquiler.DetalleAlquilerResponse.ProductoAlquiladoResponse(
                                                                                convertirProducto(detalleProducto
                                                                                                .getProducto()),
                                                                                detalleProducto.getPrecio()))
                                                                .toList()
                                                : List.of();

                return new ResponseAlquiler.DetalleAlquilerResponse(
                                detalle.getId(),

                                detalle.getEquipo() != null ? detalle.getEquipo().getId() : 0,
                                detalle.getEquipo() != null ? detalle.getEquipo().getCodigo() : "",
                                detalle.getEquipo() != null ? detalle.getEquipo().getNombre() : "",

                                productos,
                                detalle.getCantidad(),
                                detalle.getPrecioUnitario(),

                                detalle.getHorometroSalida(),
                                detalle.getHorometroRetorno(),

                                detalle.getFechaSalida(),
                                detalle.getFechaRetornoEstimada(),
                                detalle.getFechaRetorno(),
                                detalle.getLugar(),
                                detalle.getObservaciones());
        }

        private ResponseProducto convertirProducto(Producto producto) {
                if (producto == null)
                        return null;
                return new ResponseProducto(
                                producto.getId(),
                                producto.getCodigo(),
                                producto.getNombre(),
                                producto.getDescripcion(),
                                producto.getCategoria(),
                                producto.getPrecio(),
                                producto.getStock(),
                                producto.getEstado());
        }
}