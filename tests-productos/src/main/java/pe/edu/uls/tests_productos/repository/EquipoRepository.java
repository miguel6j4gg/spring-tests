package pe.edu.uls.tests_productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.uls.tests_productos.model.Equipo;
import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    List<Equipo> findByUbicacion(String nombre);

}
