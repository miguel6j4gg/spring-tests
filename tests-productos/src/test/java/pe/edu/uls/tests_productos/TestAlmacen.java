package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Almacen;
import pe.edu.uls.tests_productos.domain.repository.AlmacenRepository;

@SpringBootTest
public class TestAlmacen {

    @Autowired
    private AlmacenRepository repoAlmacen;

    @Test
    @DisplayName("1. Consulta por ID de Entity Almacen")
    void testConsultaPorIdAlmacen() {
        Almacen muestra = repoAlmacen.findAll().stream().findFirst().orElseThrow();
        Optional<Almacen> encontrado = repoAlmacen.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getCodigo()).isEqualTo(muestra.getCodigo());
        assertThat(encontrado.get().getNombre()).isNotBlank();
    }
}
