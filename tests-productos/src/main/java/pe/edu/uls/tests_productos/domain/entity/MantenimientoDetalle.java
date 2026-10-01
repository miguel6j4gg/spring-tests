package pe.edu.uls.tests_productos.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MantenimientoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_mantenimiento_detalle")
    @SequenceGenerator(name = "sec_mantenimiento_detalle", sequenceName = "sec_mantenimiento_detalle", allocationSize = 1)
    private int id;

    private int cantidad;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "mantenimiento_id", nullable = false)
    @JsonBackReference
    private Mantenimiento mantenimiento;

    @ManyToMany
    @JoinTable(name = "mantenimiento_detalle_productos", joinColumns = @JoinColumn(name = "mantenimiento_detalle_id"), inverseJoinColumns = @JoinColumn(name = "producto_id"))
    private List<Producto> productos = new ArrayList<>();

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

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}