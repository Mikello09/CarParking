package com.carpark.mls.carparking.BD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carpark.mls.carparking.AppConfig.Coche;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "CocheDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABlE_NAME = "Coche";

    private static final String DATABASE_CREATE = "create table if not exists Coche (id integer primary key autoincrement, piso TEXT, plaza TEXT, color TEXT, detalle TEXT, foto BLOB, latitud TEXT, longitud TEXT); ";

    public SQLHelper(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Claves");
        onCreate(db);

    }
    public void insertCoche(SQLiteDatabase db, String piso, String plaza, String color, String detalle, byte[] foto, String latitud, String longitud){

        String sentence = "INSERT INTO " + TABlE_NAME + "(piso, plaza, color, detalle, foto, latitud, longitud) Values ('" + piso + "','" + plaza + "','" + color + "','" + detalle + "','" + foto + "','" + latitud + "','" + longitud + "')";
        db.execSQL(sentence);

    }
    public List<Coche> getCoches(SQLiteDatabase db){

        List<Coche> listaRetornable = new ArrayList<Coche>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABlE_NAME , null);
        if (c.moveToFirst()){
            do {
                Coche coche = new Coche();
                coche.setPiso(c.getString(1));
                coche.setPlaza(c.getString(2));
                coche.setColor(c.getString(3));
                coche.setDetalles(c.getString(4));
                coche.setFoto(c.getBlob(5));
                coche.setLatitud(c.getString(6));
                coche.setLongitud(c.getString(7));
                listaRetornable.add(coche);
            } while(c.moveToNext());
        }
        c.close();
        return listaRetornable;
    }
    public void eliminarCoches(SQLiteDatabase db){
        String sentence = "DELETE FROM " + TABlE_NAME;
        db.execSQL(sentence);
    }
}
