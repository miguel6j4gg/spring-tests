package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.uls.tests_productos.domain.entity.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    List<Equipo> findByUbicacion(String ubicacion);

    boolean existsByCodigo(String codigo);

}