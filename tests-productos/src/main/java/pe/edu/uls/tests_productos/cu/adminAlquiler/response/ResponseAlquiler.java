package pe.edu.uls.tests_productos.cu.adminAlquiler.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import pe.edu.uls.tests_productos.cu.adminProducto.response.ResponseProducto;

public record ResponseAlquiler(
        int id,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado,
        int clienteId,
        String clienteNombre,
        List<DetalleAlquilerResponse> detalles

) {

    public record DetalleAlquilerResponse(
            int id,
            int equipoId,
            String codigoEquipo,
            String nombreEquipo,
            List<ResponseProducto> productos,
            int cantidad,
            double precioUnitario,
            double horometroSalida,
            double horometroRetorno,
            LocalDateTime fechaSalida,
            LocalDateTime fechaRetorno,
            String lugar) {
    }
}