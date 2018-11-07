package com.carpark.mls.carparking.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.Interfaces.DialogInterface;
import com.carpark.mls.carparking.PopUp.Dialog;
import com.carpark.mls.carparking.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Line;

import java.io.File;

public class GuardarActivity extends AppCompatActivity implements OnMapReadyCallback, DialogInterface {

    private GoogleMap map;
    private LinearLayout guardar;
    private EditText descripcion;
    private Button anadirLocation;

    //GPS VARS
    private LocationManager mLocationManager;
    private final long LOCATION_REFRESH_TIME = 1;
    private final float LOCATION_REFRESH_DISTANCE = 10;

    //DETALLES VARS
    private LinearLayout anadirDetallasImagen;
    private LinearLayout anadirDetallesTexto;
    private LinearLayout pisoLayout;
    private LinearLayout plazaLayout;
    private LinearLayout coloresLayout;
    private LinearLayout colorSeleccionado;
    private TextView plaza;
    private TextView piso;
    private String selectedColor = "negro";
    private TextView masDetallesTexto;

    //FOTO VARS
    private LinearLayout anadirFotoLayout;
    private static int RESULT_IMAGE_CLICK = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);


        onBind();
        listeners();



    }
    public void onBind(){

        pisoLayout = (LinearLayout)findViewById(R.id.pisoLayout);
        plazaLayout = (LinearLayout)findViewById(R.id.plazaLayout);
        coloresLayout = (LinearLayout)findViewById(R.id.colorLayout);
        colorSeleccionado = (LinearLayout)findViewById(R.id.colorSeleccionadoLayout);
        plaza = (TextView)findViewById(R.id.plazaTexto);
        piso = (TextView)findViewById(R.id.pisoTexto);
        anadirDetallasImagen = (LinearLayout)findViewById(R.id.anadirDetallesImagen);
        anadirDetallesTexto = (LinearLayout)findViewById(R.id.anadirDetallesTexto);
        masDetallesTexto = (TextView)findViewById(R.id.masDetallesTexto);
        anadirFotoLayout = (LinearLayout)findViewById(R.id.fotoLayout);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

    }
    public void listeners(){

        pisoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dialogoTeclado(GuardarActivity.this,"piso");
            }
        });
        plazaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dialogoTeclado(GuardarActivity.this,"plaza");
            }
        });
        coloresLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dialogoColores(GuardarActivity.this, selectedColor);
            }
        });
        anadirDetallasImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

                anadirDetallasImagen.setVisibility(View.GONE);
                anadirDetallesTexto.setVisibility(View.VISIBLE);
                anadirDetallesTexto.startAnimation(fadein);

            }
        });
        masDetallesTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(masDetallesTexto.getText().toString().equals("Mas detalles >")){
                    Dialog.dialogoMasDetalles(GuardarActivity.this,"");
                }else{
                    Dialog.dialogoMasDetalles(GuardarActivity.this,masDetallesTexto.getText().toString());
                }

            }
        });
        anadirFotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_IMAGE_CLICK);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE_CLICK) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //imageView.setImageBitmap(photo);
            }
        }else{
            //SHOW ERRROR
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        map = googleMap;

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        /*mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);*/

        LatLng sydney = new LatLng(-33.852, 151.211);
        map.addMarker(new MarkerOptions().position(sydney)
                .title("Tu coche"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Toast.makeText(GuardarActivity.this,"MAPAREADY",Toast.LENGTH_SHORT).show();
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };

    @Override
    public void seleccionarNumero(String tipo, int numero) {
        if(tipo.equals("piso")){
            piso.setText(Integer.toString(numero));
        }else{
            plaza.setText(Integer.toString(numero));
        }
    }

    @Override
    public void seleccionarColor(String color) {
        selectedColor = color;
        switch (color){
            case "negro":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.negro));
                break;
            case "azul":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.azul));
                break;
            case "rojo":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.rojo));
                break;
            case "verde":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
            case "amarillo":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.amarillo));
                break;
            case "morado":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.morado));
                break;
            case "marron":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.marron));
                break;
            case "blanco":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.blanco));
                break;
            case "gris":
                colorSeleccionado.setBackgroundColor(getResources().getColor(R.color.gris));
                break;
        }
    }

    @Override
    public void ingresarMasDetalles(String detalles) {
        masDetallesTexto.setText(detalles);
    }
}
