package pe.edu.uls.tests_productos.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    private LocalDateTime fechaRetornoEstimada;

    private LocalDateTime fechaRetorno;

    private String lugar;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "alquiler_id", nullable = false)
    @JsonBackReference
    private Alquiler alquiler;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @OneToMany(mappedBy = "alquilerDetalle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AlquilerDetalleProducto> productos = new ArrayList<>();

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

    public LocalDateTime getFechaRetornoEstimada() {
        return fechaRetornoEstimada;
    }

    public void setFechaRetornoEstimada(LocalDateTime fechaRetornoEstimada) {
        this.fechaRetornoEstimada = fechaRetornoEstimada;
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

    public List<AlquilerDetalleProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<AlquilerDetalleProducto> productos) {
        this.productos = productos;
    }

    public void agregarProducto(AlquilerDetalleProducto detalleProducto) {
        productos.add(detalleProducto);
        detalleProducto.setAlquilerDetalle(this);
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}