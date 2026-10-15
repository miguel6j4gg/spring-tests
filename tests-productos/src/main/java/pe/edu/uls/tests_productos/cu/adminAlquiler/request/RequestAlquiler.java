package pe.edu.uls.tests_productos.cu.adminAlquiler.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RequestAlquiler(

        LocalDate fechaInicio,

        LocalDate fechaFin,

        Integer estado,

        int clienteId,

        // Garantía
        double montoGarantia,

        String tipoGarantia,

        Integer estadoGarantia,

        LocalDate fechaDevolucionGarantia,

        String observacionesGarantia,

        List<DetalleAlquilerRequest> detalles

) {

    public record DetalleAlquilerRequest(

            int equipoId,

            List<Integer> productoIds,

            List<ProductoAlquilerRequest> productos,

            int cantidad,

            double precioUnitario,

            double horometroSalida,

            double horometroRetorno,

            String lugar,

            LocalDateTime fechaSalida,

            LocalDateTime fechaRetornoEstimada,

            LocalDateTime fechaRetorno,

            String observaciones

    ) {

        public record ProductoAlquilerRequest(

                int productoId,

                Double precio

        ) {
        }
    }
}