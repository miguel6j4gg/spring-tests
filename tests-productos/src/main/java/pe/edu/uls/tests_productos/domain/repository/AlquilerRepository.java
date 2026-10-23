package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.uls.tests_productos.domain.entity.Alquiler;

public interface AlquilerRepository extends JpaRepository<Alquiler, Integer> {

    // QUERY 1 (JPQL - 3 Entities: Alquiler + Entidad + AlquilerDetalle + Equipo): Alquileres por cliente/entidad y equipo
    @Query("""
            SELECT DISTINCT a FROM Alquiler a
            JOIN a.entidad e
            JOIN a.detalles d
            JOIN d.equipo eq
            WHERE e.id = :entidadId AND eq.id = :equipoId
    """)
    List<Alquiler> findAlquileresPorEntidadYEquipo(@Param("entidadId") Integer entidadId, @Param("equipoId") Integer equipoId);

    // QUERY JPQL (2 Entities: Alquiler + Entidad): Buscar alquileres por correo de entidad y estado
    @Query("""
                    SELECT DISTINCT a FROM Alquiler a
                    JOIN a.entidad e
                    WHERE LOWER(e.correo) LIKE LOWER(CONCAT('%', :correoEntidad, '%'))
                    AND a.estado = :estado
            """)
    List<Alquiler> findAlquileresPorEntidadCorreoYEstado(@Param("correoEntidad") String correoEntidad,
            @Param("estado") Integer estado);

    // QUERY 2 (JPQL - 3 Entities: Alquiler + AlquilerDetalle + Equipo): Buscar
    // alquileres por modelo de equipo
    @Query("SELECT DISTINCT a FROM Alquiler a JOIN a.detalles d JOIN d.equipo e WHERE LOWER(e.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))")
    List<Alquiler> findAlquileresPorModeloEquipo(@Param("modelo") String modelo);

    // QUERY NATIVO 1 3 Tablas: entidad, alquiler, alquiler_detalle): Resumen de
    // alquileres por entidad
    @Query(value = "SELECT e.correo AS entidad, a.id AS alquiler_id, a.estado AS estado, COUNT(d.id) AS total_detalles "
            +
            "FROM entidad e " +
            "JOIN alquiler a ON a.entidad_id = e.id " +
            "JOIN alquiler_detalle d ON d.alquiler_id = a.id " +
            "WHERE e.id = :entidadId " +
            "GROUP BY e.correo, a.id, a.estado", nativeQuery = true)
    List<Object[]> resumenAlquileresPorEntidadNativo(@Param("entidadId") Integer entidadId);
}