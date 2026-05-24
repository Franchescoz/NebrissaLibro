package com.ejemplo.mongodb.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class reseña {
    private ObjectId id;
    private int idUsuario;
    private int idLibro;
    private int puntuacion;
    private String comentario;
    private String fechaReseña;
    private List<respuestaReseña> respuestas = new ArrayList<>();

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFechaReseña() {
        return fechaReseña;
    }

    public void setFechaReseña(String fechaReseña) {
        this.fechaReseña = fechaReseña;
    }

    public List<respuestaReseña> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<respuestaReseña> respuestas) {
        this.respuestas = respuestas;
    }
}