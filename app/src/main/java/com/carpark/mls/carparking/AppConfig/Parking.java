package com.carpark.mls.carparking.AppConfig;

public class Parking {

    private String titulo;
    private String distancia;
    private Double latitude;
    private Double longitude;
    private String vicinity;
    private double rating;


    public Parking(String titulo, String distancia, Double latitude, Double longitude, String vicinity, double rating){
        this.titulo = titulo;
        this.distancia = distancia;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vicinity = vicinity;
        this.rating = rating;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
