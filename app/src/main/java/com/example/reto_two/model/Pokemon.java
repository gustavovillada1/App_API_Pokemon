package com.example.reto_two.model;

import java.io.Serializable;

public class Pokemon implements Serializable {

    private String urlFoto;
    private int idPokemon;
    private String nombre;
    private int valorDefensa, valorVelocidad, valorVida, valorAtaque;

    public Pokemon(){

    }

    public Pokemon(String urlFoto,int idPokemon , String nombre, int valorDefensa, int valorVelocidad, int valorVida, int valorAtaque) {
        this.urlFoto = urlFoto;
        this.idPokemon = idPokemon;
        this.nombre = nombre;
        this.valorDefensa = valorDefensa;
        this.valorVelocidad = valorVelocidad;
        this.valorVida = valorVida;
        this.valorAtaque = valorAtaque;
    }

    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValorDefensa() {
        return valorDefensa;
    }

    public void setValorDefensa(int valorDefensa) {
        this.valorDefensa = valorDefensa;
    }

    public int getValorVelocidad() {
        return valorVelocidad;
    }

    public void setValorVelocidad(int valorVelocidad) {
        this.valorVelocidad = valorVelocidad;
    }

    public int getValorVida() {
        return valorVida;
    }

    public void setValorVida(int valorVida) {
        this.valorVida = valorVida;
    }

    public int getValorAtaque() {
        return valorAtaque;
    }

    public void setValorAtaque(int valorAtaque) {
        this.valorAtaque = valorAtaque;
    }
}
