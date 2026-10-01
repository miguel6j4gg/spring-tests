package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.uls.tests_productos.domain.entity.Alquiler;

public interface AlquilerRepository extends JpaRepository<Alquiler, Integer> {

    // QUERY 1 (JPQL - 2 Entities: Alquiler + Cliente): Buscar alquileres por nombre
    // de cliente y estado
    @Query("""
                    SELECT DISTINCT a FROM Alquiler a
                    JOIN a.cliente c
                    WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))
                    AND LOWER(a.estado) = LOWER(:estado)
            """)
    List<Alquiler> findAlquileresPorClienteNombreYEstado(String nombreCliente, String estado);

    // QUERY 2 (JPQL - 3 Entities: Alquiler + AlquilerDetalle + Equipo): Buscar
    // alquileres por modelo de equipo
    @Query("SELECT DISTINCT a FROM Alquiler a JOIN a.detalles d JOIN d.equipo e WHERE LOWER(e.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))")
    List<Alquiler> findAlquileresPorModeloEquipo(@Param("modelo") String modelo);

    // QUERY NATIVO 1 3 Tablas: cliente, alquiler, alquiler_detalle): Resumen de
    // alquileres por cliente
    @Query(value = "SELECT c.nombre AS cliente, a.id AS alquiler_id, a.estado AS estado, COUNT(d.id) AS total_detalles "
            +
            "FROM cliente c " +
            "JOIN alquiler a ON a.cliente_id = c.id " +
            "JOIN alquiler_detalle d ON d.alquiler_id = a.id " +
            "WHERE c.id = :clienteId " +
            "GROUP BY c.nombre, a.id, a.estado", nativeQuery = true)
    List<Object[]> resumenAlquileresPorClienteNativo(@Param("clienteId") Integer clienteId);
}