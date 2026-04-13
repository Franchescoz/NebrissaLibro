package com.ejemplo.model;

import java.sql.Date;

public class libro {
    private String nombre;
    private String sinopsis;
    private String ISBN;
    private String tipo_libro;
    private Date fecha_publicacion;
    private int id_autor;


    public libro(String nombre, String sinopsis, String ISBN, String tipo_libro, Date fecha_publicacion, int id_autor) {
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.ISBN = ISBN;
        this.tipo_libro = tipo_libro;
        this.fecha_publicacion = fecha_publicacion;
        this.id_autor = id_autor;
    }

    public int getId_autor() {
        return id_autor;
    }

    public void setId_autor(int id_autor) {
        this.id_autor = id_autor;
    }

    public Date getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(Date fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getTipo_libro() {
        return tipo_libro;
    }

    public void setTipo_libro(String tipo_libro) {
        this.tipo_libro = tipo_libro;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
