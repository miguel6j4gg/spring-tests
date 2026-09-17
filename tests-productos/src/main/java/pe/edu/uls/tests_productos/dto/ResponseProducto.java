package pe.edu.uls.tests_productos.dto;

public record ResponseProducto(
        int id,
        String codigo,
        String nombre,
        String descripcion,
        String categoria,
        double precio,
        int stock,
        String estado
) {
}
