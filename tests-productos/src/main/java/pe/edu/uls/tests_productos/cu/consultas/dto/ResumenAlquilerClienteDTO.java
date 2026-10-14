package pe.edu.uls.tests_productos.cu.consultas.dto;

public record ResumenAlquilerClienteDTO(
    String cliente,
    int alquilerId,
    Integer estado,
    long totalDetalles
) {}
