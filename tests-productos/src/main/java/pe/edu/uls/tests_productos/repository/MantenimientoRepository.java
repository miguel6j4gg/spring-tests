package pe.edu.uls.tests_productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.uls.tests_productos.model.Mantenimiento;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

}