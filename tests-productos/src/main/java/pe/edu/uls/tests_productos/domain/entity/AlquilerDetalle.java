package pe.edu.uls.tests_productos.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class AlquilerDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_alquiler_detalle")
    @SequenceGenerator(name = "sec_alquiler_detalle", sequenceName = "sec_alquiler_detalle", allocationSize = 1)
    private int id;

    private int cantidad;

    private double precioUnitario;

    private double horometroSalida;

    private double horometroRetorno;

    private LocalDateTime fechaSalida;

    private LocalDateTime fechaRetorno;

    @ManyToOne
    @JoinColumn(name = "alquiler_id", nullable = false)
    private Alquiler alquiler;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getHorometroSalida() {
        return horometroSalida;
    }

    public void setHorometroSalida(double horometroSalida) {
        this.horometroSalida = horometroSalida;
    }

    public double getHorometroRetorno() {
        return horometroRetorno;
    }

    public void setHorometroRetorno(double horometroRetorno) {
        this.horometroRetorno = horometroRetorno;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalDateTime getFechaRetorno() {
        return fechaRetorno;
    }

    public void setFechaRetorno(LocalDateTime fechaRetorno) {
        this.fechaRetorno = fechaRetorno;
    }

    public Alquiler getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(Alquiler alquiler) {
        this.alquiler = alquiler;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}