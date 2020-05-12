package com.example.helpmycook;

import android.graphics.Bitmap;

class MainModel {
    Bitmap langlogo;
    String langname;

    public MainModel(Bitmap langlogo, String langname){
        this.langname = langname;
        this.langlogo = langlogo;
    }

    public Bitmap getLanglogo() {
        return langlogo;
    }

    public String getLangname() {
        return langname;
    }
}
