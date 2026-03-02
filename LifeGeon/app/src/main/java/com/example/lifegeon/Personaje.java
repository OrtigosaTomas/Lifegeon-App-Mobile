package com.example.lifegeon;

public class Personaje {

    private String nombre;
    private Integer vida;
    private Integer mana;
    private Integer defensa;

    public Personaje(String nombre, Integer vida, Integer mana, Integer defensa) {
        this.nombre = nombre;
        this.vida = vida;
        this.mana = mana;
        this.defensa = defensa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getVida() {
        return vida;
    }

    public void setVida(Integer vida) {
        this.vida = vida;
    }

    public Integer getMana() {
        return mana;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public Integer getDefensa() {
        return defensa;
    }

    public void setDefensa(Integer defensa) {
        this.defensa = defensa;
    }
}
