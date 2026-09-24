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
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_mantenimiento")
    @SequenceGenerator(
        name = "sec_mantenimiento",
        sequenceName = "sec_mantenimiento",
        allocationSize = 1
    )
    private int id;

    private LocalDate fecha;
    private String tipo;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @OneToMany(
        mappedBy = "mantenimiento",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<MantenimientoDetalle> detalles = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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
}