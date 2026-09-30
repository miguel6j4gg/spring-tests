package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.uls.tests_productos.domain.entity.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    List<Equipo> findByUbicacion(String ubicacion);

    boolean existsByCodigo(String codigo);

    // QUERY 6 (JPQL - 2 Entities: Equipo + Mantenimiento): Equipos que han tenido mantenimientos de un tipo específico
    @Query("SELECT DISTINCT e FROM Equipo e JOIN Mantenimiento m ON m.equipo = e WHERE LOWER(m.tipo) = LOWER(:tipoMantenimiento)")
    List<Equipo> findEquiposPorTipoMantenimiento(@Param("tipoMantenimiento") String tipoMantenimiento);

}