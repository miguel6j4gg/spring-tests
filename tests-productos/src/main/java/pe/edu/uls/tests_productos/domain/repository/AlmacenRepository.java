package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.uls.tests_productos.domain.entity.Almacen;

public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    List<Almacen> findByEstado(String estado);
    boolean existsByCodigo(String codigo);
}
