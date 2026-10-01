package pe.edu.uls.tests_productos.domain.entity;

import java.time.LocalDateTime;
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

    private String lugar;

    @ManyToOne
    @JoinColumn(name = "alquiler_id", nullable = false)
    @JsonBackReference
    private Alquiler alquiler;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToMany
    @JoinTable(name = "alquiler_detalle_productos", joinColumns = @JoinColumn(name = "alquiler_detalle_id"), inverseJoinColumns = @JoinColumn(name = "producto_id"))
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

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}