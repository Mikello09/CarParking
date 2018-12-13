package com.carpark.mls.carparking.UI;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carpark.mls.carparking.AppConfig.Navigator;
import com.carpark.mls.carparking.AppConfig.UserConfig;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.BD.DBOperations;
import com.carpark.mls.carparking.Interfaces.GuardarInterface;
import com.carpark.mls.carparking.PopUp.Dialog;
import com.carpark.mls.carparking.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GuardarActivity extends AppCompatActivity implements OnMapReadyCallback, GuardarInterface {


    //MAPA VARS
    private LocationManager mLocationManager;
    private final long LOCATION_REFRESH_TIME = 1;
    private final float LOCATION_REFRESH_DISTANCE = 10;
    private GoogleMap map;
    private LinearLayout mapaImagen;
    private RelativeLayout mapaDetalles;
    private TextView cerrarMapaIcono;
    private LinearLayout cerrarMapaLayout;

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
    private TextView cerrarDetallesIcono;
    private LinearLayout cerrarDetallesLayout;

    //FOTO VARS
    private LinearLayout anadirFotoLayout;
    private static int RESULT_IMAGE_CLICK = 1;
    private LinearLayout fotoLayoutDetalles;
    private LinearLayout fotoLayoutImagen;
    private ImageView foto;
    private Uri imageUri;
    private Bitmap rotatedBitmap = null;
    private TextView cerrarFotoIcono;
    private LinearLayout cerrarFotoLayout;
    private Uri fotoPath = null;

    //ANIMATIONS
    private Animation fadein;

    //GUARDAR
    private LinearLayout guardarLayout;
    private Double lastLatitude = 0.0;
    private Double lastLongitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Aparcar mi coche");

        onBind();
        listeners();

    }
    public void onBind(){

        fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        pisoLayout = (LinearLayout)findViewById(R.id.pisoLayout);
        plazaLayout = (LinearLayout)findViewById(R.id.plazaLayout);
        coloresLayout = (LinearLayout)findViewById(R.id.colorLayout);
        colorSeleccionado = (LinearLayout)findViewById(R.id.colorSeleccionadoLayout);
        plaza = (TextView)findViewById(R.id.plazaTexto);
        piso = (TextView)findViewById(R.id.pisoTexto);
        anadirDetallasImagen = (LinearLayout)findViewById(R.id.anadirDetallesImagen);
        anadirDetallesTexto = (LinearLayout) findViewById(R.id.anadirDetallesTexto);
        masDetallesTexto = (TextView)findViewById(R.id.masDetallesTexto);
        anadirFotoLayout = (LinearLayout)findViewById(R.id.fotoLayoutImagen);
        fotoLayoutDetalles = (LinearLayout) findViewById(R.id.fotoLayoutDetalles);
        foto = (ImageView)findViewById(R.id.foto);
        mapaImagen = (LinearLayout)findViewById(R.id.mapaLayoutImagen);
        mapaDetalles = (RelativeLayout)findViewById(R.id.mapaLayoutDetalles);
        guardarLayout = (LinearLayout)findViewById(R.id.guardarLayout);
        cerrarDetallesIcono = (TextView)findViewById(R.id.cerrrarDetallesIcono);
        cerrarDetallesLayout = (LinearLayout)findViewById(R.id.cerrarDetalles);
        cerrarFotoIcono = (TextView)findViewById(R.id.cerrrarFotoIcono);
        cerrarFotoLayout = (LinearLayout)findViewById(R.id.cerrarFoto);
        cerrarMapaIcono = (TextView)findViewById(R.id.cerrrarMapaIcono);
        cerrarMapaLayout = (LinearLayout)findViewById(R.id.cerrarMapa);
        fotoLayoutImagen = (LinearLayout)findViewById(R.id.fotoLayoutImagen);


        cerrarDetallesIcono.setTypeface(Utils.setFont(GuardarActivity.this,"fontawesome",true));
        cerrarFotoIcono.setTypeface(Utils.setFont(GuardarActivity.this,"fontawesome",true));
        cerrarMapaIcono.setTypeface(Utils.setFont(GuardarActivity.this,"fontawesome",true));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Navigator.NavigateToMain(GuardarActivity.this);
        return true;
    }

    public void configureMapa(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

    }
    public void listeners(){

        pisoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!piso.getText().toString().equals("-"))
                    Dialog.dialogoTeclado(GuardarActivity.this,"piso",piso.getText().toString());
                else
                    Dialog.dialogoTeclado(GuardarActivity.this,"piso","");
            }
        });
        plazaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!plaza.getText().toString().equals("-"))
                    Dialog.dialogoTeclado(GuardarActivity.this,"plaza",plaza.getText().toString());
                else
                    Dialog.dialogoTeclado(GuardarActivity.this,"plaza","");
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

                anadirDetallasImagen.setVisibility(View.GONE);
                anadirDetallesTexto.setVisibility(View.VISIBLE);
                anadirDetallesTexto.startAnimation(fadein);
                cerrarDetallesLayout.setVisibility(View.VISIBLE);
                anadirDetallasImagen.setVisibility(View.GONE);

            }
        });
        masDetallesTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(masDetallesTexto.getText().toString().equals(getResources().getString(R.string.MasDetalles))){
                    Dialog.dialogoMasDetalles(GuardarActivity.this,"");
                }else{
                    Dialog.dialogoMasDetalles(GuardarActivity.this,masDetallesTexto.getText().toString());
                }

            }
        });
        anadirFotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                permisoFoto();

            }
        });
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog.dialogoFoto(GuardarActivity.this, rotatedBitmap,null);

            }
        });
        mapaImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                permisoLocalizacion();

            }
        });
        guardarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardar();

            }
        });
        cerrarDetallesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anadirDetallesTexto.setVisibility(View.GONE);
                anadirDetallasImagen.setVisibility(View.VISIBLE);
                cerrarDetallesLayout.setVisibility(View.GONE);
                plaza.setText("-");
                piso.setText("-");
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.negro_view_seleccionado));
                masDetallesTexto.setText(R.string.MasDetalles);
            }
        });
        cerrarFotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotatedBitmap = null;
                foto.setImageBitmap(rotatedBitmap);
                fotoLayoutDetalles.setVisibility(View.GONE);
                fotoLayoutImagen.setVisibility(View.VISIBLE);
                cerrarFotoLayout.setVisibility(View.GONE);
                fotoPath = null;
            }
        });
        cerrarMapaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapaDetalles.setVisibility(View.GONE);
                mapaImagen.setVisibility(View.VISIBLE);
                map.clear();
                lastLatitude = 0.0;
                lastLongitude = 0.0;
            }
        });

    }
    public void guardar(){

        Dialog.esperaDialog(GuardarActivity.this);

        UserConfig.saveSharedPreferencesFoto(this,fotoPath);

        DBOperations.addCoche(GuardarActivity.this,
                                piso.getText().toString(),
                                plaza.getText().toString(),
                                selectedColor,
                                masDetallesTexto.getText().equals(R.string.MasDetalles) ? "" : masDetallesTexto.getText().toString(),
                                Double.toString(lastLatitude),
                                Double.toString(lastLongitude));




    }
    private void sacarFoto(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Foto");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Foto CarParking");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_IMAGE_CLICK);

    }
    private void permisoFoto(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                sacarFoto();

            } else {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }
    private void permisoLocalizacion(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mapaImagen.setVisibility(View.GONE);
                mapaDetalles.setVisibility(View.VISIBLE);
                configureMapa();

            } else {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            sacarFoto();
        }
        if(requestCode == 200){

            mapaImagen.setVisibility(View.GONE);
            mapaDetalles.setVisibility(View.VISIBLE);
            configureMapa();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE_CLICK) {

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    ExifInterface ei = new ExifInterface(getRealPathFromURI(imageUri));
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);


                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                    }

                    foto.setImageBitmap(rotatedBitmap);
                    anadirFotoLayout.setVisibility(View.GONE);
                    fotoLayoutDetalles.setVisibility(View.VISIBLE);
                    fotoLayoutDetalles.startAnimation(fadein);
                    cerrarFotoLayout.setVisibility(View.VISIBLE);
                    fotoPath = imageUri;

                } catch (Exception e) {

                    fotoLayoutDetalles.setVisibility(View.GONE);
                    anadirFotoLayout.setVisibility(View.VISIBLE);
                    cerrarFotoLayout.setVisibility(View.GONE);

                }
            }
        }else{

            fotoLayoutDetalles.setVisibility(View.GONE);
            anadirFotoLayout.setVisibility(View.VISIBLE);
            cerrarFotoLayout.setVisibility(View.GONE);

        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        map = googleMap;

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled || !network_enabled){

            Dialog.dialogoBase(GuardarActivity.this,"gpsGuardar",false,null);

        }else{
            try{

                map.setMyLocationEnabled(true);

                if (map != null) {


                    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                        @Override
                        public void onMyLocationChange(Location arg0) {

                            lastLatitude = arg0.getLatitude();
                            lastLongitude = arg0.getLongitude();

                            LatLng carLocation = new LatLng(lastLatitude, lastLongitude);
                            map.clear();
                            map.addMarker(new MarkerOptions().position(carLocation)
                                    .title("Mi coche"));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(carLocation, 16.0f));
                            //map.moveCamera(CameraUpdateFactory.newLatLng(carLocation));

                        }
                    });
                }
            }catch(SecurityException e){

                //NO HAY PERMISO
            }
        }

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
            if(numero == -1992){
                piso.setText("-");
            }else{
                piso.setText(Integer.toString(numero));
            }

        }else{
            if(numero == -1992) {
                plaza.setText("-");
            }else{
                plaza.setText(Integer.toString(numero));
            }
        }
    }

    @Override
    public void seleccionarColor(String color) {
        selectedColor = color;
        switch (color){
            case "negro":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.negro_view_seleccionado));
                break;
            case "azul":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.azul_view_seleccionado));
                break;
            case "rojo":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.rojo_view_seleccionado));
                break;
            case "verde":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.verde_view_seleccionado));
                break;
            case "amarillo":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.amarillo_view_seleccionado));
                break;
            case "morado":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.morado_view_seleccionado));
                break;
            case "marron":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.marron_view_seleccionado));
                break;
            case "blanco":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.blanco_view_seleccionado));
                break;
            case "gris":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.gris_view_seleccionado));
                break;
            case "naranja":
                colorSeleccionado.setBackground(getResources().getDrawable(R.drawable.naranja_view_seleccionado));
                break;
        }
    }

    @Override
    public void ingresarMasDetalles(String detalles) {
        masDetallesTexto.setText(detalles);
    }

    @Override
    public void cambiarFoto() {

        fotoLayoutDetalles.setVisibility(View.GONE);
        anadirFotoLayout.setVisibility(View.VISIBLE);
        rotatedBitmap = null;
        permisoFoto();
    }

    @Override
    public void esconderMapa() {

        mapaDetalles.setVisibility(View.GONE);
        mapaImagen.setVisibility(View.VISIBLE);
    }

    @Override
    public void guardando() {

        Navigator.NavigateToMain(GuardarActivity.this);

    }
}
