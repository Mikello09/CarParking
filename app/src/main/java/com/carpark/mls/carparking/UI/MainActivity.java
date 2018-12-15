package com.carpark.mls.carparking.UI;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
import com.carpark.mls.carparking.AppConfig.ResizeAnimation;
import com.carpark.mls.carparking.AppConfig.UserConfig;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.BD.DBOperations;
import com.carpark.mls.carparking.Interfaces.MainInterface;
import com.carpark.mls.carparking.Navigation.NavigationActivity;
import com.carpark.mls.carparking.PopUp.Dialog;
import com.carpark.mls.carparking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MainInterface {

    private FrameLayout mainFrame;

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
    private TextView opcionesIcono;
    private LinearLayout mapaChooseLayout;
    private LinearLayout listaChooseLayout;
    private TextView mapaIcono;
    private TextView listaIcono;
    private TextView mapaTexto;
    private TextView listaTexto;
    private LinearLayout opcionesLayout;
    private LinearLayout mapaLayout;
    private GoogleMap map;
    private List<Parking> listaParking;
    private int contadorLocationChange = 0;
    private String scrolling = "down";


    ///ERROR LAYOUT
    private LinearLayout errorLayout;
    private TextView alertIcono;
    private TextView errorText;
    private TextView reintentar;
    private ImageView imagenFondo;

    //DETAIL LAYOUT
    private TextView encontradoText;
    private LinearLayout detailLayout;
    private TextView pisoDetail;
    private TextView plazaDetail;
    private LinearLayout colorDetail;
    private TextView masDetallesDetail;
    private ImageView imagenDetail;
    private ImageView detallesImagenFondo;

    //OPCIONES LAYOUT
    private LinearLayout opcionesDetail;
    private LinearLayout cerrarOpcionesLayout;
    private TextView cerrarOpcionesIcono;
    private RadioGroup opcionesRadio;
    private RadioButton masCercanoRadio;
    private RadioButton distanciaRadio;
    private SeekBar seekbar;
    private TextView seekbarValue;
    private TextView guardarOpciones;
    private String radio;
    private LinearLayout radioLayout;

    //ANIMATIONS
    private Animation fadein;
    private Animation fadeout;
    private Animation shake;

    //INFO
    private LinearLayout infoLayout;
    private TextView infoIcono;



    private final String placesAPI = "AIzaSyDYExxjo__oIjI9cqwFkQt-2oq-kBfSdp8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//Eliminar barra superior

        onBind();

    }

    public void onBind(){

        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        mainFrame = (FrameLayout)findViewById(R.id.mainFrame);
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
        pisoDetail = (TextView)findViewById(R.id.pisoDetail);
        plazaDetail = (TextView)findViewById(R.id.plazaDetail);
        colorDetail = (LinearLayout)findViewById(R.id.colorDetail);
        masDetallesDetail = (TextView)findViewById(R.id.masDetallesDetail);
        imagenDetail = (ImageView)findViewById(R.id.imagenDetail);
        opcionesIcono = (TextView)findViewById(R.id.opcionesIcono);
        opcionesLayout = (LinearLayout)findViewById(R.id.opcionesLayout);
        mapaLayout = (LinearLayout)findViewById(R.id.mapaDetallesLayout);
        imagenFondo = (ImageView)findViewById(R.id.imagenFondoError);
        detallesImagenFondo = (ImageView)findViewById(R.id.detallesImagenFondo);
        mapaIcono = (TextView)findViewById(R.id.mapaIcono);
        listaIcono = (TextView)findViewById(R.id.listaIcono);
        mapaChooseLayout = (LinearLayout)findViewById(R.id.mapaChooseLayout);
        listaChooseLayout = (LinearLayout)findViewById(R.id.listaChooseLayout);
        mapaTexto = (TextView)findViewById(R.id.mapaTexto);
        listaTexto = (TextView)findViewById(R.id.listaTexto);
        opcionesDetail = (LinearLayout)findViewById(R.id.opcionesDetail);
        cerrarOpcionesLayout = (LinearLayout)findViewById(R.id.close_opciones);
        cerrarOpcionesIcono = (TextView)findViewById(R.id.close_opciones_icon);
        opcionesRadio = (RadioGroup)findViewById(R.id.opcionesRadio);
        masCercanoRadio = (RadioButton)findViewById(R.id.cercanosRadioButton);
        distanciaRadio = (RadioButton)findViewById(R.id.distanciaRadioButton);
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        seekbarValue = (TextView)findViewById(R.id.seekbarValue);
        guardarOpciones = (TextView)findViewById(R.id.guardarOpciones);
        radioLayout = (LinearLayout)findViewById(R.id.radioLayout);
        infoLayout = (LinearLayout)findViewById(R.id.infoLayout);
        infoIcono = (TextView)findViewById(R.id.infoIcono);


        aparcarIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        buscarIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        alertIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        titulo.setTypeface(Utils.setFont(MainActivity.this,"playfair",false));
        codebounds.setTypeface(Utils.setFont(MainActivity.this,"sofia",false));
        opcionesIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        mapaIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        listaIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        cerrarOpcionesIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));
        infoIcono.setTypeface(Utils.setFont(MainActivity.this,"fontawesome",true));

        listeners();

    }
    public void listeners(){


        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iInfo = new Intent(MainActivity.this,InfoActivity.class);
                startActivity(iInfo);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radio = Integer.toString(progress + 1) + "000";
                seekbarValue.setText(Integer.toString(progress + 1) + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        opcionesRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.cercanosRadioButton){
                    radio = UserConfig.getSharedPreferences(MainActivity.this).getRadio();
                    if(radio.equals("10000")){
                        seekbar.setProgress(10);
                    }else{
                        seekbar.setProgress(Character.getNumericValue(radio.charAt(0)) - 1);
                    }
                    radioLayout.setVisibility(View.VISIBLE);
                }else{
                    radioLayout.setVisibility(View.GONE);
                }
            }
        });

        cerrarOpcionesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoApp();
            }
        });

        guardarOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(masCercanoRadio.isChecked()){
                    UserConfig.saveSharedPreferences(MainActivity.this,null,"cercano",radio);
                }else{
                    UserConfig.saveSharedPreferences(MainActivity.this,null,"distancia",null);
                }
                estadoApp();
            }
        });


        lista.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scrolling up
                    mapaChooseLayout.setVisibility(View.GONE);
                    listaChooseLayout.setVisibility(View.GONE);
                    mapaChooseLayout.startAnimation(fadeout);
                    listaChooseLayout.startAnimation(fadeout);
                    scrolling = "up";
                } else {
                    // Scrolling down
                    if(scrolling.equals("up")){
                        mapaChooseLayout.setVisibility(View.VISIBLE);
                        listaChooseLayout.setVisibility(View.VISIBLE);
                        mapaChooseLayout.startAnimation(fadein);
                        listaChooseLayout.startAnimation(fadein);
                    }
                    scrolling = "down";
                }
            }
        });

        guardarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DBOperations.getCoches(MainActivity.this).size() == 0) {
                    ResizeAnimation resize = new ResizeAnimation(guardarLayout,
                                                guardarLayout.getWidth(),
                                                guardarLayout.getHeight(),
                                                guardarLayout.getWidth() + 30,
                                                guardarLayout.getHeight() +30);
                    Navigator.NavigateToGuardar(MainActivity.this);
                }else {
                    shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shaking);
                    guardarLayout.startAnimation(shake);
                    Dialog.dialogoBase(MainActivity.this, "eliminarCoche" ,true,null);
                }

            }
        });

        buscarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Coche> coches = DBOperations.getCoches(MainActivity.this);
                if(coches.size() != 0){
                    if(!coches.get(0).getLatitud().equals("0.0") & !coches.get(0).getLongitud().equals("0.0")) {
                        Intent iNavigation = new Intent(MainActivity.this, NavigationActivity.class);
                        iNavigation.putExtra("lat", coches.get(0).getLatitud());
                        iNavigation.putExtra("lng", coches.get(0).getLongitud());
                        startActivity(iNavigation);
                    }else{
                        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shaking);
                        buscarLayout.startAnimation(shake);
                        Utils.showSnack(mainFrame,"No hay ubicación guardada");
                    }
                }else{
                    shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shaking);
                    buscarLayout.startAnimation(shake);
                    Utils.showSnack(mainFrame,"No hay ningún coche guardado");
                }


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
                Dialog.dialogoBase(MainActivity.this, "encontrado",true,null);
            }
        });

        listaChooseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserConfig.getSharedPreferences(MainActivity.this).getModo().equals("lista")){
                    UserConfig.saveSharedPreferences(MainActivity.this,"lista",null,null);
                    estadoApp();
                }
            }
        });

        mapaChooseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserConfig.getSharedPreferences(MainActivity.this).getModo().equals("mapa")){
                    UserConfig.saveSharedPreferences(MainActivity.this,"mapa",null,null);
                    estadoApp();
                }
            }
        });


        opcionesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionesLayout();
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
                errorLayout("No estas conectado a internet","internet");
            }
        }else{
            detailLayout(coches);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void firstPageVolley(final String latitud, final String longitud){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "";
        if(UserConfig.getSharedPreferences(MainActivity.this).getOrden().equals("distancia")){
            url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitud + "," + longitud + "&rankby=distance&types=parking&sensor=false&key=" + placesAPI;
        }else{
            url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitud + "," + longitud + "&radius=" + UserConfig.getSharedPreferences(MainActivity.this).getRadio() + "&types=parking&sensor=false&key=" + placesAPI;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        obtenerLista(response,latitud,longitud,true);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorLayout("Error de red","other");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void secondPageVolley(final String latitud, final String longitud, String pageToken){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = "";
        if(UserConfig.getSharedPreferences(MainActivity.this).getOrden().equals("distancia")){
            url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitud + "," + longitud + "&pagetoken=" + pageToken +"&rankby=distance&types=parking&sensor=false&key=" + placesAPI;
        }else{
            url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitud + "," + longitud + "&pagetoken=" + pageToken + "&radius=" + UserConfig.getSharedPreferences(MainActivity.this).getRadio() + "&types=parking&sensor=false&key=" + placesAPI;
        }
        
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        obtenerLista(response,latitud,longitud,false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorLayout("Error de red","other");
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

    public void obtenerLista(String response, String latitud, String longitud, Boolean firstPage){
        try{
            if(firstPage){
                listaParking = new ArrayList<>();
            }
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
            if(firstPage){
                if(jsonObject.has("next_page_token")){
                    secondPageVolley(latitud,longitud,jsonObject.getString("next_page_token"));
                    return;
                }
            }
            if(UserConfig.getSharedPreferences(MainActivity.this).getModo().equals("lista")) {
                listaChooseLayout.setBackground(getDrawable(R.drawable.borde_seleccionado));
                mapaChooseLayout.setBackground(getDrawable(R.drawable.borde_no_seleccionado));
                ParkingListaAdapter adapter = new ParkingListaAdapter(MainActivity.this, listaParking);
                lista.setAdapter(adapter);
                lista.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mapaLayout.setVisibility(View.GONE);
                lista.setVisibility(View.VISIBLE);
                listaIcono.setTextColor(getResources().getColor(R.color.blanco));
                listaTexto.setTextColor(getResources().getColor(R.color.blanco));
                mapaIcono.setTextColor(getResources().getColor(R.color.azul));
                mapaTexto.setTextColor(getResources().getColor(R.color.azul));
            }else{
                listaChooseLayout.setBackground(getDrawable(R.drawable.borde_no_seleccionado));
                mapaChooseLayout.setBackground(getDrawable(R.drawable.borde_seleccionado));
                lista.setVisibility(View.GONE);
                mapaLayout.setVisibility(View.VISIBLE);
                listaIcono.setTextColor(getResources().getColor(R.color.azul));
                listaTexto.setTextColor(getResources().getColor(R.color.azul));
                mapaIcono.setTextColor(getResources().getColor(R.color.blanco));
                mapaTexto.setTextColor(getResources().getColor(R.color.blanco));
                configureMapa();
            }
            listaLayout();


        }catch (JSONException e){
            errorLayout("Error en JSON parse: " + e.getMessage(),"other");
        }
    }

    @Override
    public void encontrado(Boolean encontrado, android.app.Dialog dialogo) {
        dialogo.dismiss();
        if (encontrado) {
            DBOperations.eliminarCoches(MainActivity.this);
            estadoApp();
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
        firstPageVolley(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
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
        errorLayout("No tiene el GPS conectado","gps");
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
            Dialog.dialogoBase(MainActivity.this,"gpsMain",false,null);
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
            errorLayout("Error en cambiar el modo de GPS","gps");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        estadoApp();
    }

    //////INFO VISIBILITY
    public void opcionesLayout(){

        opcionesDetail.setVisibility(View.VISIBLE);
        espera.setVisibility(View.GONE);
        listaLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        if(UserConfig.getSharedPreferences(MainActivity.this).getOrden().equals("distancia")){
            distanciaRadio.setChecked(true);
            radioLayout.setVisibility(View.GONE);
        }else{
            masCercanoRadio.setChecked(true);
            radioLayout.setVisibility(View.VISIBLE);
            radio = UserConfig.getSharedPreferences(MainActivity.this).getRadio();
            if(radio.equals("10000")){
                seekbar.setProgress(10);
            }else{
                seekbar.setProgress(Character.getNumericValue(radio.charAt(0)) - 1);
            }

        }

    }

    public void esperaLayout(){
        espera.setVisibility(View.VISIBLE);
        listaLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        mapaChooseLayout.setVisibility(View.GONE);
        listaChooseLayout.setVisibility(View.GONE);
        opcionesLayout.setVisibility(View.GONE);
        opcionesDetail.setVisibility(View.GONE);
    }

    public void listaLayout(){
        espera.setVisibility(View.GONE);
        listaLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        mapaChooseLayout.setVisibility(View.VISIBLE);
        listaChooseLayout.setVisibility(View.VISIBLE);
        opcionesLayout.setVisibility(View.VISIBLE);
    }

    public void detailLayout(List<Coche> coches){
        espera.setVisibility(View.GONE);
        listaLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);
        detallesImagenFondo.setImageDrawable(getDrawable(R.drawable.car_icon_opaque));

        Coche coche = coches.get(0);

        pisoDetail.setText(coche.getPiso());
        plazaDetail.setText(coche.getPlaza());
        colorDetail.setBackground(getBacgroundColor(coche.getColor()));
        final String detalles = coche.getDetalles();
        if(detalles.equals("")){
            masDetallesDetail.setText("No hay detalles");
        }else{
            masDetallesDetail.setText(detalles);
            masDetallesDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog.dialogoBase(MainActivity.this,"detalles",null,detalles);
                }
            });
        }
        final Uri foto = UserConfig.getFotoPath(this);
        if(foto != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), foto);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                final Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                imagenDetail.setImageBitmap(bmp);
                imagenDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog.dialogoFoto(MainActivity.this, bmp);
                    }
                });
            }catch (Exception e){

            }
        }else{
            imagenDetail.setImageDrawable(getResources().getDrawable(R.mipmap.coche_icon));
            imagenDetail.setOnClickListener(null);
        }


    }

    public void configureMapa(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaDetalles);
        mapFragment.getMapAsync(this);

    }

    public void errorLayout(String errorMessage, String tipo){
        espera.setVisibility(View.GONE);
        listaLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);

        errorText.setText(errorMessage);

        switch (tipo){
            case "gps":
                imagenFondo.setImageDrawable(getDrawable(R.drawable.location_icon_opaque));
                break;
            case "internet":
                imagenFondo.setImageDrawable(getDrawable(R.drawable.wifi_icon_opaque));
                break;
            case "other":
                imagenFondo.setImageDrawable(getDrawable(R.drawable.other_icon_opaque));
                break;
        }

    }

    public Drawable getBacgroundColor(String color){
        switch (color){
            case "negro":
                return getResources().getDrawable(R.drawable.negro_view_seleccionado);
            case "azul":
                return getResources().getDrawable(R.drawable.azul_view_seleccionado);
            case "rojo":
                return getResources().getDrawable(R.drawable.rojo_view_seleccionado);
            case "verde":
                return getResources().getDrawable(R.drawable.verde_view_seleccionado);
            case "amarillo":
                return getResources().getDrawable(R.drawable.amarillo_view_seleccionado);
            case "morado":
                return getResources().getDrawable(R.drawable.morado_view_seleccionado);
            case "marron":
                return getResources().getDrawable(R.drawable.marron_view_seleccionado);
            case "blanco":
                return getResources().getDrawable(R.drawable.blanco_view_seleccionado);
            case "gris":
                return getResources().getDrawable(R.drawable.gris_view_seleccionado);
            case "naranja":
                return getResources().getDrawable(R.drawable.naranja_view_seleccionado);
            default:
                return getResources().getDrawable(R.drawable.blanco_view_seleccionado);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        contadorLocationChange = 0;
        map.clear();


        try{

            map.setMyLocationEnabled(true);

            if (map != null) {

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        opcionesLayout.setVisibility(View.VISIBLE);
                        mapaChooseLayout.setVisibility(View.VISIBLE);
                        listaChooseLayout.setVisibility(View.VISIBLE);
                        opcionesLayout.startAnimation(fadein);
                        mapaChooseLayout.startAnimation(fadein);
                        listaChooseLayout.startAnimation(fadein);
                    }
                });

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        opcionesLayout.setVisibility(View.GONE);
                        mapaChooseLayout.setVisibility(View.GONE);
                        listaChooseLayout.setVisibility(View.GONE);
                        opcionesLayout.startAnimation(fadeout);
                        mapaChooseLayout.startAnimation(fadeout);
                        listaChooseLayout.startAnimation(fadeout);
                        return false;
                    }
                });


                map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {

                        if(contadorLocationChange == 0){
                            Double myLatitude = arg0.getLatitude();
                            Double myLongitude = arg0.getLongitude();

                            LatLng myLocation = new LatLng(myLatitude, myLongitude);
                            map.clear();

                            MarkerOptions markerYo = new MarkerOptions();
                            markerYo.position(myLocation).title("Yo");
                            markerYo.icon(BitmapDescriptorFactory.fromResource(R.mipmap.image_walking));

                            map.addMarker(markerYo);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13.0f));

                            for(int i=0;i<listaParking.size();i++){
                                MarkerOptions markerParking = new MarkerOptions();
                                LatLng parkingPosition = new LatLng(listaParking.get(i).getLatitude(),listaParking.get(i).getLongitude());
                                markerParking.position(parkingPosition);
                                markerParking.icon(BitmapDescriptorFactory.fromResource(R.mipmap.parking_image));
                                markerParking.title(listaParking.get(i).getTitulo());
                                map.addMarker(markerParking);
                            }
                        }
                        contadorLocationChange ++;
                    }
                });
            }
        }catch(SecurityException e){

            //NO HAY PERMISO
        }
    }
}
