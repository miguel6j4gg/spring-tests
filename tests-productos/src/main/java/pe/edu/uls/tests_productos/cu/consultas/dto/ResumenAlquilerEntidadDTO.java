package pe.edu.uls.tests_productos.cu.consultas.dto;

public record ResumenAlquilerEntidadDTO(
    String entidad,
    int alquilerId,
    Integer estado,
    long totalDetalles
) {}
