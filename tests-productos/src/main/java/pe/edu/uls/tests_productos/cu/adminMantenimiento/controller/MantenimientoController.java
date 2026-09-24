package pe.edu.uls.tests_productos.cu.adminMantenimiento.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.uls.tests_productos.cu.adminMantenimiento.request.RequestMantenimiento;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.response.ResponseMantenimiento;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.service.MantenimientoService;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;

@RestController
@RequestMapping("/mantenimientos")
public class MantenimientoController {

        private final MantenimientoService mantenimientoService;

        public MantenimientoController(
                        MantenimientoService mantenimientoService) {

                this.mantenimientoService = mantenimientoService;
        }

        @PostMapping
        public ResponseMantenimiento registrarMantenimiento(
                        @RequestBody RequestMantenimiento request) {

                Mantenimiento mantenimiento = mantenimientoService.registrarMantenimiento(request);

                return convertirResponse(mantenimiento);
        }

        @GetMapping("/{id}")
        public ResponseMantenimiento obtenerMantenimiento(
                        @PathVariable int id) {

                Mantenimiento mantenimiento = mantenimientoService.obtenerMantenimiento(id);

                return convertirResponse(mantenimiento);
        }

        @GetMapping
        public List<ResponseMantenimiento> listarMantenimientos() {

                return mantenimientoService.listarMantenimientos()
                                .stream()
                                .map(this::convertirResponse)
                                .toList();
        }

        private ResponseMantenimiento convertirResponse(
                        Mantenimiento mantenimiento) {

                List<ResponseMantenimiento.DetalleMantenimientoResponse> detalles = mantenimiento.getDetalles()
                                .stream()
                                .map(this::convertirDetalle)
                                .toList();

                return new ResponseMantenimiento(
                                mantenimiento.getId(),
                                mantenimiento.getFecha(),
                                mantenimiento.getTipo(),
                                mantenimiento.getObservaciones(),
                                mantenimiento.getEquipo().getId(),
                                mantenimiento.getEquipo().getCodigo(),
                                detalles);
        }

        private ResponseMantenimiento.DetalleMantenimientoResponse convertirDetalle(
                        MantenimientoDetalle detalle) {

                return new ResponseMantenimiento.DetalleMantenimientoResponse(
                                detalle.getId(),
                                detalle.getProducto().getId(),
                                detalle.getProducto().getCodigo(),
                                detalle.getProducto().getNombre(),
                                detalle.getCantidad(),
                                detalle.getDescripcion());
        }
}