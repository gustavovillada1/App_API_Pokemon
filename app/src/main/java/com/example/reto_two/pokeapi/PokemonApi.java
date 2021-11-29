package com.example.reto_two.pokeapi;

import java.io.Serializable;

public class PokemonApi implements Serializable {

    private String name;
    private String id;
    private Abilities[] abilities;
    private Stats[] stats;
    private Sprites sprites;

    public PokemonApi(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Abilities[] getAbilities() {
        return abilities;
    }

    public void setAbilities(Abilities[] abilities) {
        this.abilities = abilities;
    }

    public Stats[] getStats() {
        return stats;
    }

    public void setStats(Stats[] stats) {
        this.stats = stats;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }
}
