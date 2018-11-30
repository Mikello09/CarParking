package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.content.SharedPreferences;

import com.carpark.mls.carparking.UI.MainActivity;

public class UserConfig {

    private String modo;
    private String orden;
    private String radio;

    public UserConfig(String modo, String orden, String radio){
        this.modo = modo;
        this.orden = orden;
        this.radio = radio;
    }

    public static void saveSharedPreferences(Context context, String modo, String orden, String radio){
        SharedPreferences preferences = context.getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(modo != null){
            editor.putString("modo", modo);
        }
        if(orden != null){
            editor.putString("orden", orden);
        }
        if(radio != null){
            editor.putString("radio",radio);
        }
        editor.commit();
    }

    public static UserConfig getSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
        return new UserConfig(preferences.getString("modo","lista"),preferences.getString("orden","distancia"),preferences.getString("radio","2000"));
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }



}
