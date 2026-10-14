package pe.edu.uls.tests_productos.cu.adminEmpresa.response;

public record ResponseEmpresa(
        int id,
        String ruc,
        String razonSocial,
        String contacto,
        String telefono,
        String direccion,
        String correo,
        Integer estado) {
}