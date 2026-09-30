package pe.edu.uls.tests_productos.cu.adminMantenimiento.response;

import java.time.LocalDate;
import java.util.List;

import pe.edu.uls.tests_productos.cu.adminProducto.response.ResponseProducto;

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

                        List<ResponseProducto> productos,

                        int cantidad,

                        String descripcion

        ) {
        }
}