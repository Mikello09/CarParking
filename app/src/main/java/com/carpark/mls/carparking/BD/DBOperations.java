package com.carpark.mls.carparking.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.carpark.mls.carparking.AppConfig.Coche;

import java.util.List;

public class DBOperations {

    public static void addCoche(Context context, String piso, String plaza, String color, byte[] foto, String latitud, String longitud){

        SQLHelper dbHelper = new SQLHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        dbHelper.insertCoche(database,piso,plaza,color,foto,latitud,longitud);
        database.close();

    }

    public static List<Coche> getCoches(Context context){


        SQLHelper dbHelper = new SQLHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        List<Coche> listaRetornable = dbHelper.getCoches(database);
        database.close();
        return listaRetornable;
    }

    public static void eliminarCoches(Context context){

        SQLHelper dbHelper = new SQLHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        dbHelper.eliminarCoches(database);
        database.close();

    }

}
