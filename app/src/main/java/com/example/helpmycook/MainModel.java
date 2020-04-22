package com.example.helpmycook;

class MainModel {
    Integer langlogo;
    String langname;

    public MainModel(Integer langlogo, String langname){
        this.langname = langname;
        this.langlogo = langlogo;
    }

    public int getLanglogo() {
        return langlogo;
    }

    public String getLangname() {
        return langname;
    }
}
