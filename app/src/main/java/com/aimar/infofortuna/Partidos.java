package com.aimar.infofortuna;

import java.util.Date;

public class Partidos {
    private String fechaPartido;
    private String equipo1,equipo2,resultado,category,resultado2,hora;


    public Partidos(){}
    public Partidos(String fechaPartido, String equipo1,String equipo2, String reultado) {
        this.fechaPartido = fechaPartido;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.resultado = reultado;
    }

    public String getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(String fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(String equipo1) {
        this.equipo1 = equipo1;
    }

    public String getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(String equipo2) {
        this.equipo2 = equipo2;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResultado2() {
        return resultado2;
    }

    public void setResultado2(String resultado2) {
        this.resultado2 = resultado2;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
