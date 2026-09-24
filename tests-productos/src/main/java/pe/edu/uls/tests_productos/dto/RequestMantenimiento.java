package pe.edu.uls.tests_productos.dto;

import java.time.LocalDate;
import java.util.List;

public record RequestMantenimiento(

        LocalDate fecha,

        String tipo,

        String observaciones,

        int equipoId,

        List<DetalleMantenimientoRequest> detalles

) {

    public record DetalleMantenimientoRequest(

            int productoId,

            int cantidad,

            String descripcion

    ) {
    }
}