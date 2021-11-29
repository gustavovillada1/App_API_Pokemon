package com.example.reto_two.model;

import java.io.Serializable;

public class Pokemon implements Serializable {

    private String sprites;
    private String id;
    private String idNumber;
    private String name;
    private String ability;
    private String defense, speed, hp, attack;

    public Pokemon(){

    }

    public Pokemon(String urlFoto,String idPokemon , String idNumber,  String nombre, String habilidad,String valorDefensa, String valorVelocidad, String valorVida, String valorAtaque) {
        this.sprites = urlFoto;
        this.id = idPokemon;
        this.idNumber = idNumber;
        this.name = nombre;
        this.ability =habilidad;
        this.defense = valorDefensa;
        this.speed = valorVelocidad;
        this.hp = valorVida;
        this.attack = valorAtaque;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSprites() {
        return sprites;
    }

    public void setSprites(String sprites) {
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefense() {
        return defense;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getAbility() {
        return ability;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
