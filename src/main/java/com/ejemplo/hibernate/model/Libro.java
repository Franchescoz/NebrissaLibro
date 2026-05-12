package com.ejemplo.hibernate.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "libro", schema = "nebrissabiblioteca")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Lob
    @Column(name = "sinopsis")
    private String sinopsis;

    @Column(name = "ISBN", length = 20)
    private String isbn;

    @Column(name = "tipo_libro", length = 50)
    private String tipoLibro;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTipoLibro() {
        return tipoLibro;
    }

    public void setTipoLibro(String tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

}