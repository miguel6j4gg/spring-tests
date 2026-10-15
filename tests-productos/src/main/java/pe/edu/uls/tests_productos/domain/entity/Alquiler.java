package pe.edu.uls.tests_productos.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
    @SequenceGenerator(name = "sec_alquiler", sequenceName = "sec_alquiler", allocationSize = 1)
    private int id;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer estado;

    // --- Garantía ---
    private Double montoGarantia;
    private String tipoGarantia;       // efectivo, cheque, carta_fianza
    private Integer estadoGarantia;    // 0=pendiente, 1=devuelta, 2=ejecutada
    private LocalDate fechaDevolucionGarantia;
    private String observacionesGarantia;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "alquiler", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AlquilerDetalle> detalles = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDate createdDate;

    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private LocalDate updatedDate;

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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Double getMontoGarantia() {
        return montoGarantia;
    }

    public void setMontoGarantia(Double montoGarantia) {
        this.montoGarantia = montoGarantia;
    }

    public String getTipoGarantia() {
        return tipoGarantia;
    }

    public void setTipoGarantia(String tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }

    public Integer getEstadoGarantia() {
        return estadoGarantia;
    }

    public void setEstadoGarantia(Integer estadoGarantia) {
        this.estadoGarantia = estadoGarantia;
    }

    public LocalDate getFechaDevolucionGarantia() {
        return fechaDevolucionGarantia;
    }

    public void setFechaDevolucionGarantia(LocalDate fechaDevolucionGarantia) {
        this.fechaDevolucionGarantia = fechaDevolucionGarantia;
    }

    public String getObservacionesGarantia() {
        return observacionesGarantia;
    }

    public void setObservacionesGarantia(String observacionesGarantia) {
        this.observacionesGarantia = observacionesGarantia;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
}