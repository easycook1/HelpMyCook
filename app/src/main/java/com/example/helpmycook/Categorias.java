package com.example.helpmycook;

public class Categorias {
    int id;
    String categoria;
    String icono;


    public Categorias(int id, String categoria, String icono){

        this.id = id;
        this.categoria = categoria;
        this.icono = icono;

    }

    public int getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getIcono() {
        return icono;
    }



}