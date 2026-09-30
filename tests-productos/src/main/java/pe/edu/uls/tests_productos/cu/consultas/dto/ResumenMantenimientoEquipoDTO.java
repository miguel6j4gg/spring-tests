package pe.edu.uls.tests_productos.cu.consultas.dto;

public record ResumenMantenimientoEquipoDTO(
    String codigoEquipo,
    String nombreEquipo,
    int mantenimientoId,
    String tipoMantenimiento,
    long totalProductos
) {}
