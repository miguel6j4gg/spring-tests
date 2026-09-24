package pe.edu.uls.tests_productos.cu.adminAlquiler.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

                        int productoId,

                        String codigoProducto,

                        String nombreProducto,

                        int cantidad,

                        double precioUnitario,

                        double horometroSalida,

                        double horometroRetorno,

                        LocalDateTime fechaSalida,

                        LocalDateTime fechaRetorno

        ) {
        }
}