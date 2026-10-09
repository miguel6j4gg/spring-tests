package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.uls.tests_productos.domain.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findByEstado(String estado);
    boolean existsByDni(String dni);
}
