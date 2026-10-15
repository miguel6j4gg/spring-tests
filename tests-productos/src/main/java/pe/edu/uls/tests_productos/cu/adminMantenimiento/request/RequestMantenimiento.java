package pe.edu.uls.tests_productos.cu.adminMantenimiento.request;

import java.time.LocalDate;
import java.util.List;

public record RequestMantenimiento(

                LocalDate fechaSolicitud,

                LocalDate fechaProgramada,

                LocalDate fechaRealizado,

                String tipo,

                String observaciones,

                int equipoId,

                Integer empleadoId,

                List<DetalleMantenimientoRequest> detalles

) {

        public record DetalleMantenimientoRequest(

                        List<Integer> productoIds,

                        int cantidad,

                        String descripcion,

                        String observaciones

        ) {
        }
}