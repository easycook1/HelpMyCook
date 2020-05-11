package com.example.helpmycook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context){
        this.openHelper= new DatabaseOpenHelper(context);
    }


    //retorna la instancia de la base
    public static DatabaseAccess getInstance(Context context){
        if(instance == null){
            instance=new DatabaseAccess(context);

        }
        return instance;
    }

    //abre la db
    public void open(){
        this.db=openHelper.getWritableDatabase();
    }


    //cierra base
    public void close(){
        if (db!=null){
            this.db.close();
        }
    }

    /*
    //query

    public String getAddress(String name){

        ArrayList <String> listaCate = new ArrayList<>();

    //    c=db.rawQuery("select descripcion from ingredientes where id = '"+name+"'", new String[]{});
        c=db.rawQuery("select descripcion from categorias ", new String[]{});
        StringBuffer buffer = new StringBuffer();

        while (c.moveToNext()){

         //   listaCate.add(c.getString(0));

            String address = c.getString(0);
            buffer.append(""+address);
        }
        return buffer.toString();
    }
//clase vector

*/

    public ArrayList<Categorias> lista(String name){

        ArrayList<Categorias> categorias = new ArrayList<Categorias>();

        c=db.rawQuery("select id, descripcion, icono from categorias ", null);


        while (c.moveToNext()){

               categorias.add(new Categorias(c.getInt(0), c.getString(1), c.getString(2)));

        }
        return categorias;
    }

}
