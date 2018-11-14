package com.carpark.mls.carparking.UI;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.R;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar espera;
    private TextView codebounds;
    private ImageView carIcono;

    private int progreso = 0;

    //ANIMATIONS
    private Animation fadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();//Eliminar barra superior

        onBind();
        esperaHandler();


    }
    public void onBind(){

        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_splash);


        espera = (ProgressBar)findViewById(R.id.espera);
        codebounds = (TextView)findViewById(R.id.codebounds_text_splash);
        carIcono = (ImageView)findViewById(R.id.carIcono);

        espera.setProgress(0);

        codebounds.setTypeface(Utils.setFont(SplashActivity.this,"sofia",false));

        carIcono.startAnimation(fadein);

    }
    public void esperaHandler(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rellenarProgreso();
            }
        }, 20);
    }
    public void rellenarProgreso(){


        if(progreso > 100){
            goToMain();
        }else{
            espera.setProgress(progreso);
            progreso += 1;
            esperaHandler();
        }



        /*switch (progreso){
            case 0:

            case 5:

                break;
            case 10:
                break;
            case 15:
                break;
            case 20:
                break;
            case 25:
                break;
            case 30:
                break;
            case 35:
                break;
            case 40:
                break;
            case 45:
                break;
            case 50:
                break;
            case 55:
                break;
            case 60:
                break;
            case 65:
                break;
            case 70:
                break;
            case 75:
                break;
            case 80:
                break;
            case 85:
                break;
            case 90:
                break;
            case 95:
                break;
            case 100:
                break;

        }*/
    }
    public void goToMain(){
        Navigator.NavigateToMain(SplashActivity.this);
    }
}
