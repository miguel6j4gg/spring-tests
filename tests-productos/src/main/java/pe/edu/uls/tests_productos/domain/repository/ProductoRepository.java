package pe.edu.uls.tests_productos.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.uls.tests_productos.domain.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoria(String categoria);

    boolean existsByCodigo(String codigo);

    @Query("""
                SELECT DISTINCT p
                FROM AlquilerDetalle ad
                JOIN ad.productos p
                JOIN ad.alquiler a
                WHERE a.fechaInicio >= :fechaInicio AND a.fechaFin <= :fechaFin
            """)
    List<Producto> findProductosAlquiladosEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
}
