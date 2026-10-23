package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Empleado;
import pe.edu.uls.tests_productos.domain.repository.EmpleadoRepository;

@SpringBootTest
public class TestEmpleado {

    @Autowired
    private EmpleadoRepository repoEmpleado;

    @Test
    @DisplayName("2. Consulta por ID de Entity Empleado")
    void testConsultaPorIdEmpleado() {
        Empleado muestra = repoEmpleado.findAll().stream().findFirst().orElseThrow();
        Optional<Empleado> encontrado = repoEmpleado.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getDni()).isEqualTo(muestra.getDni());
        assertThat(encontrado.get().getNombre()).isNotBlank();
    }
}
