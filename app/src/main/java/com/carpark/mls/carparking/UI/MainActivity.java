package com.carpark.mls.carparking.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carpark.mls.carparking.AppConfig.CustomLocation;
import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.Parking;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.BD.DBOperations;
import com.carpark.mls.carparking.Interfaces.EliminarInterface;
import com.carpark.mls.carparking.Interfaces.LocationInterface;
import com.carpark.mls.carparking.PopUp.Dialog;
import com.carpark.mls.carparking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EliminarInterface,LocationInterface {

    private TextView aparcarIcono;
    private TextView buscarIcono;
    private TextView titulo;
    private TextView codebounds;

    private LinearLayout guardarLayout;
    private LinearLayout buscarLayout;

    private RecyclerView lista;
    private ProgressBar espera;

    private final String placesAPI = "AIzaSyDYExxjo__oIjI9cqwFkQt-2oq-kBfSdp8";

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
        lista = (RecyclerView) findViewById(R.id.lista);
        espera = (ProgressBar)findViewById(R.id.esperaLista);


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
            permisoLocalizacion();

        }else{
            permisoLocalizacion();
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
                        try{
                            List<Parking> listaParking = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray routesArray = jsonObject.getJSONArray("results");
                            for(int i=0;i<routesArray.length();i++){
                                JSONObject route = routesArray.getJSONObject(i);
                                JSONObject geometry = route.getJSONObject("geometry");
                                JSONObject location = geometry.getJSONObject("location");
                                Parking p = new Parking(route.getString("name"), "500",
                                                        location.getDouble("lat"),
                                                        location.getDouble("lng"),
                                                        route.getString("vicinity"),
                                                        route.getDouble("rating"));
                                listaParking.add(p);
                            }
                            ParkingListaAdapter adapter = new ParkingListaAdapter(MainActivity.this, listaParking);
                            lista.setAdapter(adapter);
                            lista.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            listaLayout();
                        }catch (JSONException e){
                            Log.d("","");
                        }


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
        esperaLayout();
        pruebaVolley(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
    }

    @Override
    public void activarGPS() {
        isGpsEnabled();
    }

    private void permisoLocalizacion(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isGpsEnabled();

            } else {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200){
            isGpsEnabled();
        }
        if(requestCode == 300){
            getLocationMode();
        }
    }
    public void isGpsEnabled(){
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocationMode();
        }else{
            Dialog.dialogoGPSMain(MainActivity.this);
        }
    }
    public void getLocationMode()
    {
        try {
            if (Settings.Secure.getInt(MainActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE) != 3) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),300);
            }else{
                CustomLocation customLocation = new CustomLocation(MainActivity.this);
                customLocation.getActualLocation();
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        estadoApp();
    }

    //////INFO VISIBILITY
    public void esperaLayout(){
        espera.setVisibility(View.VISIBLE);
        lista.setVisibility(View.GONE);
    }

    public void listaLayout(){
        espera.setVisibility(View.GONE);
        lista.setVisibility(View.VISIBLE);
    }
}
