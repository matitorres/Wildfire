package com.example.wildfire.models;

public class Recurso {

    private int RecursoId, institucionId;
    private String nombre;
    private boolean estado;

    public Recurso(int recursoId, int institucionId, String nombre, boolean estado) {
        RecursoId = recursoId;
        this.institucionId = institucionId;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getRecursoId() {
        return RecursoId;
    }

    public void setRecursoId(int recursoId) {
        RecursoId = recursoId;
    }

    public int getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(int institucionId) {
        this.institucionId = institucionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
