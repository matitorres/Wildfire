package com.example.wildfire.models;

import java.util.List;

public class Brigada {

    private int brigadaId, numero;
    private String tipo, estado;
    private List<RecursoBrigada> recursosBrigada;
    private List<Integrante> integrantes;

    public Brigada(int brigadaId, int numero, String tipo, String estado, List<RecursoBrigada> recursosBrigada, List<Integrante> integrantes) {
        this.brigadaId = brigadaId;
        this.numero = numero;
        this.tipo = tipo;
        this.estado = estado;
        this.recursosBrigada = recursosBrigada;
        this.integrantes = integrantes;
    }

    public int getBrigadaId() {
        return brigadaId;
    }

    public void setBrigadaId(int brigadaId) {
        this.brigadaId = brigadaId;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<RecursoBrigada> getRecursosBrigada() {
        return recursosBrigada;
    }

    public void setRecursosBrigada(List<RecursoBrigada> recursosBrigada) {
        this.recursosBrigada = recursosBrigada;
    }

    public List<Integrante> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Integrante> integrantes) {
        this.integrantes = integrantes;
    }
}
