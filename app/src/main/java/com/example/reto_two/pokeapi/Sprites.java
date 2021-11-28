package com.example.reto_two.pokeapi;

public class Sprites {

    private String front_default;

    public Sprites() {
    }

    public Sprites(String front_default) {
        this.front_default = front_default;
    }


    public String getFront_default() {
        return front_default;
    }

    public void setFront_default(String front_default) {
        this.front_default = front_default;
    }
}
