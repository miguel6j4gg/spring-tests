package pe.edu.uls.tests_productos.dto;

import java.time.LocalDate;
import java.util.List;

public record ResponseMantenimiento(

        int id,

        LocalDate fecha,

        String tipo,

        String observaciones,

        int equipoId,

        String codigoEquipo,

        List<DetalleMantenimientoResponse> detalles

) {

    public record DetalleMantenimientoResponse(

            int id,

            int productoId,

            String codigoProducto,

            String nombreProducto,

            int cantidad,

            String descripcion

    ) {
    }
}