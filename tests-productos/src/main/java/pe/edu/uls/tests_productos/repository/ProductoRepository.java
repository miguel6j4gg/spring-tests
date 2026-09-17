package pe.edu.uls.tests_productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.uls.tests_productos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoria(String categoria);

    boolean existsByCodigo(String codigo);

}
