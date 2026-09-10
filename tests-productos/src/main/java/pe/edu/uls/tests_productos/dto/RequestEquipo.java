package pe.edu.uls.tests_productos.dto;

public record RequestEquipo(
		String codigo,
		String nombre,
		String modelo,
		double potencia,
		double horometro,
		String ubicacion,
		String combustible,
		String estado) {
}