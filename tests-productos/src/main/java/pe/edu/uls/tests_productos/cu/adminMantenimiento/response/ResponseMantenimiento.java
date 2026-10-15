package pe.edu.uls.tests_productos.cu.adminMantenimiento.response;

import java.time.LocalDate;
import java.util.List;

import pe.edu.uls.tests_productos.cu.adminProducto.response.ResponseProducto;

public record ResponseMantenimiento(

                int id,

                LocalDate fechaSolicitud,

                LocalDate fechaProgramada,

                LocalDate fechaRealizado,

                String tipo,

                String observaciones,

                int equipoId,

                String codigoEquipo,

                Integer empleadoId,

                List<DetalleMantenimientoResponse> detalles

) {

        public record DetalleMantenimientoResponse(

                        int id,

                        List<ResponseProducto> productos,

                        int cantidad,

                        String descripcion,

                        String observaciones

        ) {
        }
}