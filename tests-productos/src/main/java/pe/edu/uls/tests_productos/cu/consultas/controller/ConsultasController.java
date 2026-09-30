package pe.edu.uls.tests_productos.cu.consultas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.uls.tests_productos.cu.consultas.dto.ResumenAlquilerClienteDTO;
import pe.edu.uls.tests_productos.cu.consultas.dto.ResumenMantenimientoEquipoDTO;
import pe.edu.uls.tests_productos.cu.consultas.service.ConsultasService;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.Producto;

@RestController
@RequestMapping("/api/consultas")
public class ConsultasController {

    private final ConsultasService consultasService;

    public ConsultasController(ConsultasService consultasService) {
        this.consultasService = consultasService;
    }

    // 1. JPQL: Buscar alquileres por cliente y estado
    @GetMapping("/jpql/alquileres-cliente-estado")
    public List<Alquiler> buscarAlquileresPorClienteYEstado(
            @RequestParam String nombreCliente,
            @RequestParam String estado) {
        return consultasService.buscarAlquileresPorClienteYEstado(nombreCliente, estado);
    }

    // 2. JPQL: Buscar alquileres por modelo de equipo
    @GetMapping("/jpql/alquileres-modelo-equipo")
    public List<Alquiler> buscarAlquileresPorModeloEquipo(
            @RequestParam String modelo) {
        return consultasService.buscarAlquileresPorModeloEquipo(modelo);
    }

    // 3. JPQL: Buscar mantenimientos por ubicación del equipo
    @GetMapping("/jpql/mantenimientos-ubicacion")
    public List<Mantenimiento> buscarMantenimientosPorUbicacion(
            @RequestParam String ubicacion) {
        return consultasService.buscarMantenimientosPorUbicacion(ubicacion);
    }

    // 4. JPQL: Buscar mantenimientos por código de producto cambiado
    @GetMapping("/jpql/mantenimientos-producto")
    public List<Mantenimiento> buscarMantenimientosPorCodigoProducto(
            @RequestParam String codigoProducto) {
        return consultasService.buscarMantenimientosPorCodigoProducto(codigoProducto);
    }

    // 5. JPQL: Buscar productos alquilados en rango de fechas
    @GetMapping("/jpql/productos-alquilados-fechas")
    public List<Producto> buscarProductosAlquiladosEnFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return consultasService.buscarProductosAlquiladosEnFechas(fechaInicio, fechaFin);
    }

    // 6. JPQL: Buscar equipos por tipo de mantenimiento
    @GetMapping("/jpql/equipos-tipo-mantenimiento")
    public List<Equipo> buscarEquiposPorTipoMantenimiento(
            @RequestParam String tipoMantenimiento) {
        return consultasService.buscarEquiposPorTipoMantenimiento(tipoMantenimiento);
    }

    // 7. NATIVO 1: Resumen de alquileres por cliente
    @GetMapping("/nativas/resumen-alquileres-cliente/{clienteId}")
    public List<ResumenAlquilerClienteDTO> resumenAlquileresPorCliente(
            @PathVariable Integer clienteId) {
        return consultasService.resumenAlquileresPorCliente(clienteId);
    }

    // 8. NATIVO 2: Resumen de mantenimientos por equipo
    @GetMapping("/nativas/resumen-mantenimientos-equipo/{equipoId}")
    public List<ResumenMantenimientoEquipoDTO> resumenMantenimientosPorEquipo(
            @PathVariable Integer equipoId) {
        return consultasService.resumenMantenimientosPorEquipo(equipoId);
    }
}
