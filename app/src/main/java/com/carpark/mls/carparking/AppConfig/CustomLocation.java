package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.carpark.mls.carparking.Interfaces.DialogInterface;
import com.carpark.mls.carparking.Interfaces.LocationInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CustomLocation {

    private FusedLocationProviderClient mFusedLocationClient;
    private Context context;



    public CustomLocation(Context context){
        this.context = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);
    }

    private static LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(200000);
        mLocationRequest.setFastestInterval(300000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    public void getActualLocation(){

        if ( ContextCompat.checkSelfPermission( context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            final LocationInterface interfazEspera = (LocationInterface) context;
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        interfazEspera.localizacion(location);
                    }else{
                        Utils.showToast(context,"Location null");
                    }
                }
            });
        }else{
            Utils.showToast(context,"No hay permisos");
        }

    }

}
