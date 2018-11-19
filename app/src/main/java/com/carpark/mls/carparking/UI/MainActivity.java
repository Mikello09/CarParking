package com.carpark.mls.carparking.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carpark.mls.carparking.AppConfig.CustomLocation;
import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.BD.DBOperations;
import com.carpark.mls.carparking.Interfaces.EliminarInterface;
import com.carpark.mls.carparking.Interfaces.LocationInterface;
import com.carpark.mls.carparking.PopUp.Dialog;
import com.carpark.mls.carparking.R;


public class MainActivity extends AppCompatActivity implements EliminarInterface,LocationInterface {

    private TextView aparcarIcono;
    private TextView buscarIcono;
    private TextView titulo;
    private TextView codebounds;

    private LinearLayout guardarLayout;
    private LinearLayout buscarLayout;


    private final String placesAPI = "AIzaSyDYExxjo__oIjI9cqwFkQt-2oq-kBfSdp8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//Eliminar barra superior

        onBind();
        estadoApp();
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

                if(DBOperations.getCoches(MainActivity.this).size() == 0) {
                    Navigator.NavigateToGuardar(MainActivity.this);
                }else {
                    Dialog.eliminarCocheDialog(MainActivity.this);
                }

            }
        });

    }

    public void estadoApp(){

        if(DBOperations.getCoches(MainActivity.this).size() == 0){

        }else{
            CustomLocation customLocation = new CustomLocation(MainActivity.this);
            customLocation.getActualLocation();
        }

    }

    @Override
    public void onBackPressed() {
        return;
    }
    public void pruebaVolley(String latitud, String longitud){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitud + "," + longitud + "&radius=5000&types=parking&sensor=false&key=" + placesAPI;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Utils.showToast(MainActivity.this,"OK: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(MainActivity.this,"ERROR: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void eliminarAparcamiento(Boolean eliminar, android.app.Dialog dialogo) {

        if (eliminar){
            DBOperations.eliminarCoches(MainActivity.this);
            Navigator.NavigateToGuardar(MainActivity.this);
        }else{
            dialogo.dismiss();
        }
    }

    @Override
    public void localizacion(Location location) {
        pruebaVolley(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
    }
}
