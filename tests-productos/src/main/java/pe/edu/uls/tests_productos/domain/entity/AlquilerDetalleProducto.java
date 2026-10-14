package pe.edu.uls.tests_productos.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "alquiler_detalle_productos")
public class AlquilerDetalleProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_alquiler_detalle_producto")
    @SequenceGenerator(name = "sec_alquiler_detalle_producto", sequenceName = "sec_alquiler_detalle_producto", allocationSize = 1)
    private int id;

    @ManyToOne
    @JoinColumn(name = "alquiler_detalle_id", nullable = false)
    @JsonBackReference
    private AlquilerDetalle alquilerDetalle;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private double precio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AlquilerDetalle getAlquilerDetalle() {
        return alquilerDetalle;
    }

    public void setAlquilerDetalle(AlquilerDetalle alquilerDetalle) {
        this.alquilerDetalle = alquilerDetalle;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
