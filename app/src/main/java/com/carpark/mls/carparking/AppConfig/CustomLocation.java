package com.carpark.mls.carparking.AppConfig;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.carpark.mls.carparking.Interfaces.MainInterface;

public class CustomLocation {

    private Context context;
    private LocationManager locationManager;
    private Criteria criteria;
    private LocationListener locationListener;


    public CustomLocation(Context context){
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);


    }

    public void getActualLocation(){

        final MainInterface interfazEspera = (MainInterface) context;


        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                interfazEspera.localizacion(location);
                locationManager.removeUpdates(locationListener);
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        if (ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location lastLocationKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastLocationKnown != null){
                interfazEspera.localizacion(lastLocationKnown);
            }

        }


    }


}
