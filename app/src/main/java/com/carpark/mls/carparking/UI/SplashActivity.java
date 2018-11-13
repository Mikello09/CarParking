package com.carpark.mls.carparking.UI;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.R;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar espera;
    private TextView codebounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();//Eliminar barra superior

        onBind();
        esperaHandler25();


    }
    public void onBind(){

        espera = (ProgressBar)findViewById(R.id.espera);
        codebounds = (TextView)findViewById(R.id.codebounds_text_splash);

        espera.setProgress(0);

        codebounds.setTypeface(Utils.setFont(SplashActivity.this,"sofia",false));

    }
    public void esperaHandler25(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                espera.setProgress(25);
                esperaHandler50();
            }
        }, 500);
    }
    public void esperaHandler50(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                espera.setProgress(50);
                esperaHandler75();
            }
        }, 500);
    }
    public void esperaHandler75(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                espera.setProgress(75);
                esperaHandler100();
            }
        }, 500);
    }
    public void esperaHandler100(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                espera.setProgress(100);
                goToMain();
            }
        }, 500);
    }
    public void goToMain(){
        Navigator.NavigateToMain(SplashActivity.this);
    }
}
