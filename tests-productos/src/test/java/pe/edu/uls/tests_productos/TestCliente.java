package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;

@SpringBootTest
public class TestCliente {

    @Autowired
    private ClienteRepository repoCliente;

    @Test
    @DisplayName("4. Consulta por ID de Entity Cliente")
    void testConsultaPorIdCliente() {
        Cliente muestra = repoCliente.findAll().stream().findFirst().orElseThrow();
        Optional<Cliente> encontrado = repoCliente.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getDocumento()).isEqualTo(muestra.getDocumento());
        assertThat(encontrado.get().getNombre()).isNotBlank();
    }
}
