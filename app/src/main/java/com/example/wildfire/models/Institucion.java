package com.example.wildfire.models;

import java.util.List;

public class Institucion {

    private int institucionId;
    private String latitud, longitud, nombre, descripcion;
    private boolean estado;
    private List<Recurso> recursos;
    private List<Brigadista> brigadistas;

    public Institucion(int institucionId, String latitud, String longitud, String nombre, String descripcion, boolean estado, List<Recurso> recursos, List<Brigadista> brigadistas) {
        this.institucionId = institucionId;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.recursos = recursos;
        this.brigadistas = brigadistas;
    }

    public int getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(int institucionId) {
        this.institucionId = institucionId;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    public List<Brigadista> getBrigadistas() {
        return brigadistas;
    }

    public void setBrigadistas(List<Brigadista> brigadistas) {
        this.brigadistas = brigadistas;
    }
}
