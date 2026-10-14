package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.uls.tests_productos.domain.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    @Query("SELECT e FROM Empresa e JOIN e.entidad en WHERE en.estado = :estado")
    List<Empresa> findByEstado(@Param("estado") Integer estado);

    boolean existsByRuc(String ruc);
}