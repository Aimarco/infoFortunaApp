package com.example.temporal.infofortuna;

public class Coche {
    private String Nombre;
    private int numViajes;


    public Coche(){}

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getNumViajes() {
        return numViajes;
    }

    public void setNumViajes(int numViajes) {
        this.numViajes = numViajes;
    }
}
