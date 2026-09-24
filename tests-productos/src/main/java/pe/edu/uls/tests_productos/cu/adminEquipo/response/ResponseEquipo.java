package pe.edu.uls.tests_productos.cu.adminEquipo.response;

public record ResponseEquipo(
		int id,
		String codigo,
		String nombre,
		String modelo,
		double potencia,
		double horometro,
		String ubicacion,
		String combustible,
		String estado) {
}