package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.graphics.Typeface;
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

    public static boolean hasInternetAccess()
    {
        try
        {
            URL url = new URL("http://www.google.com");

            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "Android Application:1");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000 * 30);
            urlc.connect();

            // http://www.w3.org/Protocols/HTTP/HTRESP.html
            if (urlc.getResponseCode() == 200 || urlc.getResponseCode() > 400)
            {
                // Requested site is available
                return true;
            }
        }
        catch (Exception ex)
        {
            // Error while trying to connect
            return false;
        }
        return false;
    }
}
