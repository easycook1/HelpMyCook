package com.example.helpmycook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    //query
    public String getAddress(String name){


        c=db.rawQuery("select * from where name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();

        while (c.moveToNext()){
            String address = c.getString(0);
            buffer.append(""+address);
        }
        return buffer.toString();


    }

}
