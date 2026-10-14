package pe.edu.uls.tests_productos.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.uls.tests_productos.domain.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    List<Proveedor> findByEstado(Integer estado);
    List<Proveedor> findByEmpresa_Id(Integer empresaId);
    boolean existsByRuc(String ruc);
}
