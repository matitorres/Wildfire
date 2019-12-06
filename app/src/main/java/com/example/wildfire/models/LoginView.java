package com.example.wildfire.models;

public class LoginView {

    private String Mail, Clave;

    public LoginView(String mail, String clave) {
        this.Mail = mail;
        this.Clave = clave;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        this.Mail = mail;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        this.Clave = clave;
    }
}
