package com.example.reto_two.model;

import java.io.Serializable;
import java.util.ArrayList;

public class EntrenadorPokemon implements Serializable {

    private String username;


    public static final int USUARIO_EXISTE = 1;
    public static final int USUARIO_NO_EXISTE = 2;

    public EntrenadorPokemon() {

    }

    public EntrenadorPokemon(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
