package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.repository.EntidadRepository;

@SpringBootTest
public class TestEntidad {

    @Autowired
    private EntidadRepository repoEntidad;

    @Test
    @DisplayName("6. Consulta por ID de Entity Entidad")
    void testConsultaPorIdEntidad() {
        Entidad muestra = repoEntidad.findAll().stream().findFirst().orElseThrow();
        Optional<Entidad> encontrado = repoEntidad.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getTelefono()).isNotBlank();
    }
}
