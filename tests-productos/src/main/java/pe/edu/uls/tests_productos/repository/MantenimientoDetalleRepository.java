package pe.edu.uls.tests_productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.uls.tests_productos.model.MantenimientoDetalle;

public interface MantenimientoDetalleRepository
        extends JpaRepository<MantenimientoDetalle, Integer> {

}