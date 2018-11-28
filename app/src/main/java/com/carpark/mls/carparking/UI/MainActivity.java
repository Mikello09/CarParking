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
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carpark.mls.carparking.AppConfig.Coche;
import com.carpark.mls.carparking.AppConfig.CustomLocation;
import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.Parking;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.BD.DBOperations;
import com.carpark.mls.carparking.Interfaces.EliminarInterface;
import com.carpark.mls.carparking.Interfaces.LocationInterface;
import com.carpark.mls.carparking.Navigation.NavigationActivity;
import com.carpark.mls.carparking.PopUp.Dialog;
import com.carpark.mls.carparking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EliminarInterface,LocationInterface {

    private TextView aparcarIcono;
    private TextView buscarIcono;
    private TextView titulo;
    private TextView codebounds;

    private LinearLayout guardarLayout;
    private LinearLayout buscarLayout;

    ////LISTA LAYOUT
    private RecyclerView lista;
    private ProgressBar espera;
    private LinearLayout listaLayout;


    ///ERROR LAYOUT
    private LinearLayout errorLayout;
    private TextView alertIcono;
    private TextView errorText;
    private TextView reintentar;

    //DETAIL LAYOUT
    private TextView encontradoText;

    private LinearLayout detailLayout;

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
        listaLayout = (LinearLayout)findViewById(R.id.listaDetail);
        errorLayout = (LinearLayout) findViewById(R.id.errorDetail);
        detailLayout = (LinearLayout)findViewById(R.id.infoDetail);
        alertIcono = (TextView)findViewById(R.id.warningIcono);
        errorText = (TextView)findViewById(R.id.errorTexto);
        reintentar = (TextView)findViewById(R.id.reintentarTexto);
        encontradoText = (TextView)findViewById(R.id.encontradoText);


        aparcarIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        buscarIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        alertIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
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
                    Dialog.eliminarCocheDialog(MainActivity.this,true);
                }

            }
        });

        buscarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNavigation = new Intent(MainActivity.this,NavigationActivity.class);
                startActivity(iNavigation);
            }
        });

        reintentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoApp();
            }
        });

        encontradoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.eliminarCocheDialog(MainActivity.this,false);
            }
        });

    }

    public void estadoApp(){

        esperaLayout();
        List<Coche> coches = DBOperations.getCoches(MainActivity.this);
        if(coches.size() == 0) {
            if (Utils.hasInternetAccess(MainActivity.this)) {
                permisoLocalizacion();
            } else {
                errorLayout("No estas conectado a internet");
            }
        }else{
            detailLayout(coches);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void pruebaVolley(final String latitud, final String longitud){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitud + "," + longitud + "&radius=5000&types=parking&sensor=false&key=" + placesAPI;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        obtenerLista(response,latitud,longitud);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorLayout("Error en volley: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public String calcularDistancia(double lat2, double lng2, String latS1, String lngS1){
        double lat1 = Double.parseDouble(latS1);
        double lng1 = Double.parseDouble(lngS1);

        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);

        float distance = loc1.distanceTo(loc2)/1000;

        return new DecimalFormat("##.###").format(distance);
    }

    public void obtenerLista(String response, String latitud, String longitud){
        try{
            List<Parking> listaParking = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray routesArray = jsonObject.getJSONArray("results");
            for(int i=0;i<routesArray.length();i++){
                JSONObject route = routesArray.getJSONObject(i);
                JSONObject geometry = route.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                String vicinity = route.has("vicinity") ? route.getString("vicinity") : "";
                Double rating = route.has("rating") ? route.getDouble("rating") : 0;

                Parking p = new Parking(route.getString("name"),
                        calcularDistancia(location.getDouble("lat"),location.getDouble("lng"),latitud,longitud),
                        location.getDouble("lat"),
                        location.getDouble("lng"),
                        vicinity,
                        rating);
                listaParking.add(p);
            }
            ParkingListaAdapter adapter = new ParkingListaAdapter(MainActivity.this, listaParking);
            lista.setAdapter(adapter);
            lista.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            listaLayout();
        }catch (JSONException e){
            errorLayout("Error en JSON parse: " + e.getMessage());
        }
    }

    @Override
    public void eliminarAparcamiento(Boolean eliminar, android.app.Dialog dialogo, Boolean guardar) {

        if (eliminar){
            DBOperations.eliminarCoches(MainActivity.this);
            if(guardar) {
                Navigator.NavigateToGuardar(MainActivity.this);
            }else{
                dialogo.dismiss();
                estadoApp();
            }
        }else{
            dialogo.dismiss();
        }
    }

    @Override
    public void localizacion(Location location) {
        pruebaVolley(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
    }

    @Override
    public void activarGPS() {
        isGpsEnabled();
    }

    @Override
    public void activarInternet() {
        estadoApp();
    }

    @Override
    public void cancelarGPS() {
        errorLayout("No tiene el GPS conectado");
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
            errorLayout("Error en cambiar el modo de GPS");
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
        listaLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
    }

    public void listaLayout(){
        espera.setVisibility(View.GONE);
        listaLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
    }

    public void detailLayout(List<Coche> coches){
        espera.setVisibility(View.GONE);
        listaLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);
    }

    public void errorLayout(String errorMessage){
        espera.setVisibility(View.GONE);
        listaLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);

        errorText.setText(errorMessage);
    }


}
