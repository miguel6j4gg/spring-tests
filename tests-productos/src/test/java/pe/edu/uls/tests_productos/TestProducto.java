package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@SpringBootTest
public class TestProducto {

    @Autowired
    private ProductoRepository repoProducto;

    @Test
    @DisplayName("8. Consulta por ID de Entity Producto")
    void testConsultaPorIdProducto() {
        Producto muestra = repoProducto.findAll().stream().findFirst().orElseThrow();
        Optional<Producto> encontrado = repoProducto.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getCodigo()).isEqualTo(muestra.getCodigo());
        assertThat(encontrado.get().getPrecio()).isPositive();
    }
}
