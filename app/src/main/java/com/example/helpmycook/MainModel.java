package com.example.helpmycook;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

class MainModel {
    Drawable langlogo;
    String langname;

    public MainModel(Drawable langlogo, String langname){
        this.langname = langname;
        this.langlogo = langlogo;
    }

    public Drawable getLanglogo() {
        return langlogo;
    }

    public String getLangname() {
        return langname;
    }
}
