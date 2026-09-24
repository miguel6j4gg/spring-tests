package pe.edu.uls.tests_productos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RequestAlquiler(

        LocalDate fechaInicio,

        LocalDate fechaFin,

        String estado,

        int clienteId,

        List<DetalleAlquilerRequest> detalles

) {

    public record DetalleAlquilerRequest(

            int equipoId,

            int productoId,

            int cantidad,

            double precioUnitario,

            double horometroSalida,

            double horometroRetorno,

            LocalDateTime fechaSalida,

            LocalDateTime fechaRetorno

    ) {
    }
}