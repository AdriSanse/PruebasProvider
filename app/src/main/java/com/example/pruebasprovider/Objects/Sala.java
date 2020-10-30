package com.example.pruebasprovider.Objects;

import java.util.ArrayList;
import java.util.List;

public class Sala {
    String id, nombreCreador, nombreSala, contrasena;
    String dinero;
    List<String> grupo = new ArrayList<String>();
    List<Sucesos> sucesos = new ArrayList<Sucesos>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCreador() {
        return nombreCreador;
    }

    public void setNombreCreador(String nombreCreador) {
        this.nombreCreador = nombreCreador;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List getGrupo() {
        return grupo;
    }

    public void setGrupo(List grupo) {
        this.grupo = grupo;
    }

    public String getDinero() {
        return dinero;
    }

    public void setDinero(String dinero) {
        this.dinero = dinero;
    }

    public List<Sucesos> getSucesos() {
        return sucesos;
    }

    public void setSucesos(List<Sucesos> sucesos) {
        this.sucesos = sucesos;
    }
}
