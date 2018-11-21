package com.carpark.mls.carparking.AppConfig;

public class Parking {

    private String titulo;
    private String distancia;


    public Parking(String titulo, String distancia){
        this.titulo = titulo;
        this.distancia = distancia;
    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

}
