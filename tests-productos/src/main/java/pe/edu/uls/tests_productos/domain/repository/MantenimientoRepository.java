package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    @Query("SELECT DISTINCT m FROM Mantenimiento m LEFT JOIN FETCH m.detalles WHERE m.id = :id")
    java.util.Optional<Mantenimiento> findByIdWithDetalles(@Param("id") Integer id);

    // QUERY 3 (JPQL con Join - 2 Entities: Mantenimiento + Equipo): Mantenimientos
    // por ubicación del equipo
    @Query("SELECT DISTINCT m FROM Mantenimiento m JOIN m.equipo e WHERE LOWER(e.ubicacion) = LOWER(:ubicacion)")
    List<Mantenimiento> findMantenimientosPorUbicacionEquipo(@Param("ubicacion") String ubicacion);

    // QUERY 4 (JPQL con Join - 3 Entities: Mantenimiento + MantenimientoDetalle +
    // Producto): Mantenimientos por código de producto cambiado
    @Query("SELECT DISTINCT m FROM Mantenimiento m JOIN m.detalles d JOIN d.productos p WHERE LOWER(p.codigo) = LOWER(:codigoProducto)")
    List<Mantenimiento> findMantenimientosPorCodigoProducto(@Param("codigoProducto") String codigoProducto);

    // QUERY 5 (JPQL con Join - 3 Entities: Mantenimiento + Empleado + Equipo):
    // Mantenimientos realizados por un técnico según su DNI y el tipo de
    // mantenimiento
    @Query("SELECT DISTINCT m FROM Mantenimiento m JOIN m.empleado emp JOIN m.equipo eq WHERE LOWER(emp.dni) = LOWER(:dni) AND LOWER(m.tipo) = LOWER(:tipo)")
    List<Mantenimiento> findMantenimientosPorTecnicoYTipo(
            @Param("dni") String dni,
            @Param("tipo") String tipo);

    // QUERY NATIVO 3 (Native Query - 5 Tablas: equipo, mantenimiento,
    // mantenimiento_detalle, mantenimiento_detalle_productos, producto):
    // Resumen de productos consumidos en los mantenimientos de un equipo
    @Query(value = "SELECT p.codigo, p.nombre, COUNT(DISTINCT md.id) AS total_usos "
            +
            "FROM equipo e " +
            "JOIN mantenimiento m ON m.equipo_id = e.id " +
            "JOIN mantenimiento_detalle md ON md.mantenimiento_id = m.id " +
            "JOIN mantenimiento_detalle_productos mdp ON mdp.mantenimiento_detalle_id = md.id " +
            "JOIN producto p ON p.id = mdp.producto_id " +
            "WHERE LOWER(e.codigo) = LOWER(:codigoEquipo) " +
            "GROUP BY p.codigo, p.nombre " +
            "ORDER BY COUNT(DISTINCT md.id) DESC", nativeQuery = true)
    List<Object[]> resumenProductosConsumidosPorEquipo(@Param("codigoEquipo") String codigoEquipo);

    // QUERY NATIVO 2 (Native Query - 4 Tablas: mantenimiento, equipo,
    // mantenimiento_detalle, mantenimiento_detalle_productos)
    @Query(value = "SELECT e.codigo AS codigo_equipo, e.nombre AS nombre_equipo, m.id AS mantenimiento_id, m.tipo AS tipo_mantenimiento, COUNT(mdp.producto_id) AS total_productos "
            +
            "FROM mantenimiento m " +
            "JOIN equipo e ON m.equipo_id = e.id " +
            "JOIN mantenimiento_detalle md ON md.mantenimiento_id = m.id " +
            "JOIN mantenimiento_detalle_productos mdp ON mdp.mantenimiento_detalle_id = md.id " +
            "WHERE e.id = :equipoId " +
            "GROUP BY e.codigo, e.nombre, m.id, m.tipo", nativeQuery = true)
    List<Object[]> resumenMantenimientoEquipoNativo(@Param("equipoId") Integer equipoId);
}