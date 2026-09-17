package pe.edu.uls.tests_productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.uls.tests_productos.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByEstado(String estado);

    boolean existsByDocumento(String documento);

}
