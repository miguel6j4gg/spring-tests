package pe.edu.uls.tests_productos.cu.adminAlquiler.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestActualizarAlquiler(
        LocalDate fechaFin,
        LocalDateTime fechaRetorno,
        Double horometroRetorno,
        Integer estado,
        Integer estadoGarantia,
        LocalDate fechaDevolucionGarantia,
        String observacionesGarantia
) {
}
