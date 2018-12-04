package com.carpark.mls.carparking.Interfaces;

import android.location.Location;

public interface MainInterface {

    void eliminarAparcamiento(Boolean eliminar, android.app.Dialog dialogo, Boolean guardar);

    void localizacion(Location location);

    void activarGPS();

    void activarInternet();

    void cancelarGPS();

}
