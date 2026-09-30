package pe.edu.uls.tests_productos.cu.consultas.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.cu.consultas.dto.ResumenAlquilerClienteDTO;
import pe.edu.uls.tests_productos.cu.consultas.dto.ResumenMantenimientoEquipoDTO;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@Service
public class ConsultasService {

    private final AlquilerRepository alquilerRepository;
    private final MantenimientoRepository mantenimientoRepository;
    private final ProductoRepository productoRepository;
    private final EquipoRepository equipoRepository;

    public ConsultasService(
            AlquilerRepository alquilerRepository,
            MantenimientoRepository mantenimientoRepository,
            ProductoRepository productoRepository,
            EquipoRepository equipoRepository) {
        this.alquilerRepository = alquilerRepository;
        this.mantenimientoRepository = mantenimientoRepository;
        this.productoRepository = productoRepository;
        this.equipoRepository = equipoRepository;
    }

    // JPQL 1: Alquileres por nombre de cliente y estado
    public List<Alquiler> buscarAlquileresPorClienteYEstado(String nombreCliente, String estado) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El parámetro 'nombreCliente' no puede estar vacío");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El parámetro 'estado' no puede estar vacío");
        }
        return alquilerRepository.findAlquileresPorClienteNombreYEstado(nombreCliente.trim(), estado.trim());
    }

    // JPQL 2: Alquileres por modelo de equipo
    public List<Alquiler> buscarAlquileresPorModeloEquipo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El parámetro 'modelo' no puede estar vacío");
        }
        return alquilerRepository.findAlquileresPorModeloEquipo(modelo.trim());
    }

    // JPQL 3: Mantenimientos por ubicación de equipo
    public List<Mantenimiento> buscarMantenimientosPorUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El parámetro 'ubicacion' no puede estar vacío");
        }
        return mantenimientoRepository.findMantenimientosPorUbicacionEquipo(ubicacion.trim());
    }

    // JPQL 4: Mantenimientos por código de producto
    public List<Mantenimiento> buscarMantenimientosPorCodigoProducto(String codigoProducto) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El parámetro 'codigoProducto' no puede estar vacío");
        }
        return mantenimientoRepository.findMantenimientosPorCodigoProducto(codigoProducto.trim());
    }

    // JPQL 5: Productos alquilados en rango de fechas
    public List<Producto> buscarProductosAlquiladosEnFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las fechas 'fechaInicio' y 'fechaFin' no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La 'fechaInicio' no puede ser posterior a 'fechaFin'");
        }
        return productoRepository.findProductosAlquiladosEnRangoFechas(fechaInicio, fechaFin);
    }

    // JPQL 6: Equipos por tipo de mantenimiento
    public List<Equipo> buscarEquiposPorTipoMantenimiento(String tipoMantenimiento) {
        if (tipoMantenimiento == null || tipoMantenimiento.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El parámetro 'tipoMantenimiento' no puede estar vacío");
        }
        return equipoRepository.findEquiposPorTipoMantenimiento(tipoMantenimiento.trim());
    }

    // NATIVO 1: Resumen alquileres por cliente
    public List<ResumenAlquilerClienteDTO> resumenAlquileresPorCliente(Integer clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El 'clienteId' debe ser un entero mayor a 0");
        }
        List<Object[]> resultados = alquilerRepository.resumenAlquileresPorClienteNativo(clienteId);
        return resultados.stream().map(row -> new ResumenAlquilerClienteDTO(
                (String) row[0],
                ((Number) row[1]).intValue(),
                (String) row[2],
                ((Number) row[3]).longValue()
        )).toList();
    }

    // NATIVO 2: Resumen mantenimientos por equipo
    public List<ResumenMantenimientoEquipoDTO> resumenMantenimientosPorEquipo(Integer equipoId) {
        if (equipoId == null || equipoId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El 'equipoId' debe ser un entero mayor a 0");
        }
        List<Object[]> resultados = mantenimientoRepository.resumenMantenimientoEquipoNativo(equipoId);
        return resultados.stream().map(row -> new ResumenMantenimientoEquipoDTO(
                (String) row[0],
                (String) row[1],
                ((Number) row[2]).intValue(),
                (String) row[3],
                ((Number) row[4]).longValue()
        )).toList();
    }
}
