package pe.edu.uls.tests_productos.cu.adminCliente.response;

public record ResponseCliente(
                int id,
                String documento,
                String nombre,
                String apellido,
                String email,
                String telefono,
                String direccion,
                String estado) {
}
