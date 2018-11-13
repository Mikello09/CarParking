package com.carpark.mls.carparking.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.R;

public class MainActivity extends AppCompatActivity {

    private TextView aparcarIcono;
    private TextView buscarIcono;
    private TextView titulo;
    private TextView codebounds;

    private LinearLayout guardarLayout;
    private LinearLayout buscarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//Eliminar barra superior

        onBind();
    }

    public void onBind(){

        aparcarIcono = (TextView)findViewById(R.id.guardarCocheIcono);
        buscarIcono = (TextView)findViewById(R.id.dondeEstaIcono);
        titulo = (TextView)findViewById(R.id.titulo_text);
        codebounds = (TextView)findViewById(R.id.codebounds_text);
        guardarLayout = (LinearLayout)findViewById(R.id.guardarCocheLayout);
        buscarLayout = (LinearLayout)findViewById(R.id.dondeEstaLayout);


        aparcarIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        buscarIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        titulo.setTypeface(Utils.setFont(MainActivity.this,"playfair",false));
        codebounds.setTypeface(Utils.setFont(MainActivity.this,"sofia",false));

        listeners();

    }
    public void listeners(){

        guardarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigator.NavigateToGuardar(MainActivity.this);

            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }
}
