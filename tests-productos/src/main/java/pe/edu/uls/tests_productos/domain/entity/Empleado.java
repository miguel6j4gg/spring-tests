package pe.edu.uls.tests_productos.domain.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_empleado")
    @SequenceGenerator(name = "sec_empleado", sequenceName = "sec_empleado", allocationSize = 1)
    private int id;

    @Column(unique = true, nullable = false)
    private String dni;

    private String nombre;
    private String apellido;
    private String cargo;
    private String telefono;
    private String email;
    private Integer estado;

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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
}
