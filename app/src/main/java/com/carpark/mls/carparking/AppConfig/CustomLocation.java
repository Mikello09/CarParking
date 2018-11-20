package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.carpark.mls.carparking.Interfaces.LocationInterface;

public class CustomLocation {

    private Context context;
    private LocationManager locationManager;


    public CustomLocation(Context context){
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }



    public void getActualLocation(){

        final LocationInterface interfazEspera = (LocationInterface) context;


        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                interfazEspera.localizacion(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        if (ContextCompat.checkSelfPermission( context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }


    }

}
