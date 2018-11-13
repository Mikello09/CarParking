package com.carpark.mls.carparking.AppConfig;

public class Coche {

    private String piso;
    private String plaza;
    private String color;
    private String foto;
    private String latitud;
    private String longitud;

    public Coche(){

    }
    public Coche(String pi, String pl, String co, String fo, String lat, String lon){

        this.piso = pi;
        this.plaza = pl;
        this.color = co;
        this.foto = fo;
        this.latitud = lat;
        this.longitud = lon;

    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public void setPlaza(String plaza) {
        this.plaza = plaza;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public String getPiso() {
        return piso;
    }

    public String getPlaza() {
        return plaza;
    }

    public String getColor() {
        return color;
    }

    public String getFoto() {
        return foto;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }




}
