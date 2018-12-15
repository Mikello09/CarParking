package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {


    public static Typeface setFont(Context context, String fontName, Boolean icon){
        if(icon){
            String path = "fonts/" +fontName + ".ttf";
            return Typeface.createFromAsset(context.getAssets(), path);
        }else{
            String path = "fonts/" +fontName + ".otf";
            return Typeface.createFromAsset(context.getAssets(), path);
        }
    }

    public static void showToast(Context context, String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }

    public static boolean hasInternetAccess(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }else {
            return false;
        }
    }

    public static void showSnack(FrameLayout frame, LinearLayout linear, String mensaje){
        if(frame == null){
            Snackbar snackbar = Snackbar.make(linear, mensaje, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else{
            Snackbar snackbar = Snackbar.make(frame, mensaje, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
