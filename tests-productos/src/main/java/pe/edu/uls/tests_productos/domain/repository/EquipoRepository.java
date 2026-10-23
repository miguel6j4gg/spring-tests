package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import pe.edu.uls.tests_productos.domain.entity.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    List<Equipo> findByUbicacion(String ubicacion);

    boolean existsByCodigo(String codigo);

    // Caso A:
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Equipo e WHERE e.id = :id")
    Optional<Equipo> findByIdWithLock(@Param("id") Integer id);

    // Caso B:
    @Query("SELECT e FROM Equipo e WHERE e.id = :id")
    Optional<Equipo> findByIdSinLock(@Param("id") Integer id);

    @Query("SELECT DISTINCT e FROM Equipo e JOIN Mantenimiento m ON m.equipo = e WHERE LOWER(m.tipo) = LOWER(:tipoMantenimiento)")
    List<Equipo> findEquiposPorTipoMantenimiento(@Param("tipoMantenimiento") String tipoMantenimiento);

}