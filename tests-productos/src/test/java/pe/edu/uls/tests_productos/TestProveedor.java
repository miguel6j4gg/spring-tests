package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Proveedor;
import pe.edu.uls.tests_productos.domain.repository.ProveedorRepository;

@SpringBootTest
public class TestProveedor {

    @Autowired
    private ProveedorRepository repoProveedor;

    @Test
    @DisplayName("3. Consulta por ID de Entity Proveedor")
    void testConsultaPorIdProveedor() {
        Proveedor muestra = repoProveedor.findAll().stream().findFirst().orElseThrow();
        Optional<Proveedor> encontrado = repoProveedor.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getRuc()).isEqualTo(muestra.getRuc());
        assertThat(encontrado.get().getRazonSocial()).isNotBlank();
    }
}
