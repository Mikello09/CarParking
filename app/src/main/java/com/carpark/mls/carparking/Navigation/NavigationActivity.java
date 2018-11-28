package com.carpark.mls.carparking.Navigation;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carpark.mls.carparking.AppConfig.Utils;
import com.carpark.mls.carparking.PopUp.Dialog;
import com.carpark.mls.carparking.R;
import com.carpark.mls.carparking.UI.GuardarActivity;
import com.carpark.mls.carparking.UI.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private LocationManager mLocationManager;
    private String directionsApiKey = "AIzaSyDYExxjo__oIjI9cqwFkQt-2oq-kBfSdp8";

    Double lastLatitude = 0.0;
    Double lastLongitude = 0.0;

    Double destinationLatitude = 0.0;
    Double destinationLongitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        getExtras();
        configureMap();
    }

    public void getExtras(){

        destinationLatitude = getIntent().getExtras().getDouble("lat");
        destinationLongitude = getIntent().getExtras().getDouble("lng");
    }

    public void configureMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaNavigation);
        mapFragment.getMapAsync(this);
    }

    public void directionsVolley(){
        RequestQueue queue = Volley.newRequestQueue(NavigationActivity.this);
        String url ="https://maps.googleapis.com/maps/api/directions/json?origin=" + lastLatitude + "," + lastLongitude + "&destination=" + destinationLatitude + "," + destinationLongitude + "&sensor=false&key=" + directionsApiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            List<List<HashMap<String,String>>> result = parse(jsonObject);
                            ArrayList<LatLng> points;
                            PolylineOptions lineOptions = null;
                            for (int i = 0; i < result.size(); i++) {
                                points = new ArrayList<>();
                                lineOptions = new PolylineOptions();
                                List<HashMap<String, String>> path = result.get(i);
                                for (int j = 0; j < path.size(); j++) {
                                    HashMap<String, String> point = path.get(j);
                                    double lat = Double.parseDouble(point.get("lat"));
                                    double lng = Double.parseDouble(point.get("lng"));
                                    LatLng position = new LatLng(lat, lng);
                                    points.add(position);
                                }
                                lineOptions.addAll(points);
                                lineOptions.width(10);
                                lineOptions.color(Color.RED);
                            }
                            map.addPolyline(lineOptions);
                        }catch (JSONException e){
                            Utils.showToast(NavigationActivity.this,"JSON ERROR: " + e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(NavigationActivity.this,"ERROR: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        //mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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

            Dialog.dialogoGPS(NavigationActivity.this);

        }else{
            try{

                map.setMyLocationEnabled(true);

                if (map != null) {

                    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                        @Override
                        public void onMyLocationChange(Location arg0) {

                            float distancia = calcularDistancia(lastLatitude,lastLongitude,arg0.getLatitude(),arg0.getLongitude());
                            if(distancia > 10) {

                                map.clear();
                                lastLatitude = arg0.getLatitude();
                                lastLongitude = arg0.getLongitude();

                                LatLng yoLocation = new LatLng(lastLatitude, lastLongitude);
                                map.addMarker(new MarkerOptions().position(yoLocation).title("Yo"));

                                LatLng parkingLocation = new LatLng(destinationLatitude, destinationLongitude);
                                map.addMarker(new MarkerOptions().position(parkingLocation).title("Parking"));

                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(yoLocation, 16.0f));

                                directionsVolley();
                            }

                        }
                    });
                }
            }catch(SecurityException e){

                //NO HAY PERMISO
            }
        }

    }


    public List<List<HashMap<String,String>>> parse(JSONObject jObject){
        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();
                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);
                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude) );
                            hm.put("lng", Double.toString((list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public float calcularDistancia(double lat2, double lng2, double latS1, double lngS1){
        double lat1 = latS1;
        double lng1 = lngS1;

        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);

        float distance = loc1.distanceTo(loc2);

        return distance;
    }
}
