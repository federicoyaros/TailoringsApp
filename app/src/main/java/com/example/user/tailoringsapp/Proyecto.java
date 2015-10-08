package com.example.user.tailoringsapp;

/**
 * Created by User on 05/09/2015.
 */
public class Proyecto {

    private String nombre;
    private String responsable;
    private String socio;
    private String metodologia;
    private Boolean activo;

    public Proyecto(String nombre, String responsable, String socio, String metodologia){
        this.nombre = nombre;
        this.responsable = responsable;
        this.socio = socio;
        this.metodologia = metodologia;
        this.activo = true;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getSocio() {
        return socio;
    }

    public void setSocio(String socio) {
        this.socio = socio;
    }

    public String getMetodologia() {
        return metodologia;
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
