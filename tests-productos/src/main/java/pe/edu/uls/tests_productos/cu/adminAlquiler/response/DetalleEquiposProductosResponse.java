package pe.edu.uls.tests_productos.cu.adminAlquiler.response;

import java.util.List;

public record DetalleEquiposProductosResponse(
        int alquilerId,
        List<ItemDetalleResponse> detalles
) {
    public record ItemDetalleResponse(
            int detalleId,
            int equipoId,
            String equipoCodigo,
            String equipoNombre,
            String equipoModelo,
            double horometroSalida,
            double horometroRetorno,
            List<ProductoAsociadoResponse> productos
    ) {
    }

    public record ProductoAsociadoResponse(
            int productoId,
            String productoCodigo,
            String productoNombre,
            double precio
    ) {
    }
}
