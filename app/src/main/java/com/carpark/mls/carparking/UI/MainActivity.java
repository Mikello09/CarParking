package com.carpark.mls.carparking.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carpark.mls.carparking.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//Eliminar barra superior

        Intent iMap = new Intent(MainActivity.this,GuardarActivity.class);
        startActivity(iMap);
    }
}
