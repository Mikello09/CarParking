package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.graphics.Typeface;

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
}
