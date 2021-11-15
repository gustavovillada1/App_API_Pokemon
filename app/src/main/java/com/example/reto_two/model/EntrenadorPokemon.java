package com.example.reto_two.model;

import java.io.Serializable;
import java.util.ArrayList;

public class EntrenadorPokemon implements Serializable {

    private String username;
    private ArrayList<Pokemon> pokemons;


    public static final int USUARIO_EXISTE=1;
    public static final int USUARIO_NO_EXISTE=2;

    public EntrenadorPokemon(){

    }

    public EntrenadorPokemon(String username, ArrayList<Pokemon> pokemons) {
        this.username = username;
        this.pokemons = pokemons;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }
}
