package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

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
}
