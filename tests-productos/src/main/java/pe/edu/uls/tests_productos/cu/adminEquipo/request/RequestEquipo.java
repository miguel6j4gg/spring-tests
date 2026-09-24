package pe.edu.uls.tests_productos.cu.adminEquipo.request;

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