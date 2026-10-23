package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.entity.Empresa;
import pe.edu.uls.tests_productos.domain.repository.EmpresaRepository;

@SpringBootTest
public class TestEmpresa {

    @Autowired
    private EmpresaRepository repoEmpresa;

    @Test
    @DisplayName("5. Consulta por ID de Entity Empresa")
    void testConsultaPorIdEmpresa() {
        Empresa muestra = repoEmpresa.findAll().stream().findFirst().orElseGet(() -> {
            Empresa nueva = new Empresa();
            nueva.setRuc("20999888771");
            nueva.setRazonSocial("Empresa Demo Test S.A.C.");
            nueva.setContacto("Gerente General");
            Entidad ent = new Entidad();
            ent.setTelefono("911223344");
            ent.setCorreo("contacto@empresademo.pe");
            ent.setDireccion("Av. Test 100");
            ent.setEstado(1);
            nueva.setEntidad(ent);
            return repoEmpresa.save(nueva);
        });

        Optional<Empresa> encontrado = repoEmpresa.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getRuc()).isEqualTo(muestra.getRuc());
    }
}
