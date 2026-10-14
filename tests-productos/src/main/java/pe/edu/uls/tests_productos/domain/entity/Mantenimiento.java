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
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_mantenimiento")
    @SequenceGenerator(name = "sec_mantenimiento", sequenceName = "sec_mantenimiento", allocationSize = 1)
    private int id;

    private LocalDate fechaMantenimientoRealizado;
    private String tipo;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = true)
    private Empleado empleado;

    @OneToMany(mappedBy = "mantenimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MantenimientoDetalle> detalles = new ArrayList<>();

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

    public LocalDate getFechaMantenimientoRealizado() {
        return fechaMantenimientoRealizado;
    }

    public void setFechaMantenimientoRealizado(LocalDate fechaMantenimientoRealizado) {
        this.fechaMantenimientoRealizado = fechaMantenimientoRealizado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<MantenimientoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<MantenimientoDetalle> detalles) {
        this.detalles = detalles;
    }

    public void agregarDetalle(MantenimientoDetalle detalle) {
        detalles.add(detalle);
        detalle.setMantenimiento(this);
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
}