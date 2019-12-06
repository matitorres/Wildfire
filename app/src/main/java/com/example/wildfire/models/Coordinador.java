package com.example.wildfire.models;

public class Coordinador {

    private int coordinadorId;
    private String nombre, mail, clave, salt, telefono;
    private boolean estado;

    public Coordinador() {}

    public Coordinador(int coordinadorId, String nombre, String mail, String clave, String salt, String telefono, boolean estado) {
        this.coordinadorId = coordinadorId;
        this.nombre = nombre;
        this.mail = mail;
        this.clave = clave;
        this.salt = salt;
        this.telefono = telefono;
        this.estado = estado;
    }

    public int getCoordinadorId() {
        return coordinadorId;
    }

    public void setCoordinadorId(int coordinadorId) {
        this.coordinadorId = coordinadorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
