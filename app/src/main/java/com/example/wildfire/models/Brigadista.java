package com.example.wildfire.models;

public class Brigadista {

    private int brigadistaId, institucionId;
    private String nombre, funcion, mail, clave, salt, rol, telefono;
    private boolean estado;

    public int getBrigadistaId() {
        return brigadistaId;
    }

    public void setBrigadistaId(int brigadistaId) {
        this.brigadistaId = brigadistaId;
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

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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
