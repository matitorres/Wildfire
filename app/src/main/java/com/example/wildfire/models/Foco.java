package com.example.wildfire.models;

import java.util.List;

public class Foco {

    private int focoId, coordinadorId;
    private String latitud, longitud, estado, descripcion;
    private Coordinador coordinador;
    private List<Brigada> brigadas;

    public int getFocoId() {
        return focoId;
    }

    public void setFocoId(int focoId) {
        this.focoId = focoId;
    }

    public int getCoordinadorId() {
        return coordinadorId;
    }

    public void setCoordinadorId(int coordinadorId) {
        this.coordinadorId = coordinadorId;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    public List<Brigada> getBrigadas() {
        return brigadas;
    }

    public void setBrigadas(List<Brigada> brigadas) {
        this.brigadas = brigadas;
    }
}
