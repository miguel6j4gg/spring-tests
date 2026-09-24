package pe.edu.uls.tests_productos.cu.adminCliente.request;

public record RequestCliente(
                String documento,
                String nombre,
                String apellido,
                String email,
                String telefono,
                String direccion,
                String estado) {
}
