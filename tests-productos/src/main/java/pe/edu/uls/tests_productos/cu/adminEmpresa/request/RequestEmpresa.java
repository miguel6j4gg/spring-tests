package pe.edu.uls.tests_productos.cu.adminEmpresa.request;

public record RequestEmpresa(
        String ruc,
        String razonSocial,
        String contacto,
        String telefono,
        String direccion,
        String correo,
        Integer estado) {
}