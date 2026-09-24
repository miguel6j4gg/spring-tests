package pe.edu.uls.tests_productos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MantenimientoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_mantenimiento_detalle")
    @SequenceGenerator(
        name = "sec_mantenimiento_detalle",
        sequenceName = "sec_mantenimiento_detalle",
        allocationSize = 1
    )
    private int id;

    private int cantidad;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "mantenimiento_id", nullable = false)
    private Mantenimiento mantenimiento;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Mantenimiento getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}