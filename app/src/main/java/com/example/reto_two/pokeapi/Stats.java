package com.example.reto_two.pokeapi;

public class Stats {

    private Stat stat;
    private String base_stat;

    public Stats(){

    }

    public String getBase_stat() {
        return base_stat;
    }

    public void setBase_stat(String base_stat) {
        this.base_stat = base_stat;
    }

    public Stats(Stat stat) {
        this.stat = stat;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }
}
