package com.carpark.mls.carparking.AppConfig;

import android.content.Context;
import android.content.Intent;

import com.carpark.mls.carparking.UI.GuardarActivity;
import com.carpark.mls.carparking.UI.MainActivity;

public class Navigator {

    public static void NavigateToMain(Context context){

        Intent iMain = new Intent(context,MainActivity.class);
        context.startActivity(iMain);

    }
    public static void NavigateToGuardar(Context context){

        Intent iGuardar = new Intent(context,GuardarActivity.class);
        context.startActivity(iGuardar);

    }
}
