package pe.edu.uls.tests_productos.cu.adminProducto.request;

public record RequestProducto(
                String codigo,
                String nombre,
                String descripcion,
                String categoria,
                double precio,
                int stock,
                String estado) {
}
