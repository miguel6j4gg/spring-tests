package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.uls.tests_productos.domain.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("SELECT c FROM Cliente c JOIN c.entidad e WHERE e.estado = :estado")
    List<Cliente> findByEstado(@Param("estado") Integer estado);

    boolean existsByDocumento(String documento);

}
