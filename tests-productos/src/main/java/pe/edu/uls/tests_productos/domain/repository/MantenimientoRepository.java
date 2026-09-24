package pe.edu.uls.tests_productos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

}