package pe.edu.uls.tests_productos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_alquiler")
    @SequenceGenerator(
        name = "sec_alquiler",
        sequenceName = "sec_alquiler",
        allocationSize = 1
    )
    private int id;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(
        mappedBy = "alquiler",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<AlquilerDetalle> detalles = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<AlquilerDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<AlquilerDetalle> detalles) {
        this.detalles = detalles;
    }

    public void agregarDetalle(AlquilerDetalle detalle) {
        detalles.add(detalle);
        detalle.setAlquiler(this);
    }
}