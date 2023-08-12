package com.example.n01371888_zhangzhiwang_owm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String apiKey = "aee081ed5c53912a682b0bd5ec420281";

    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap mMap;
    private LatLng currentLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button zoomInButton = findViewById(R.id.zoom_in_button);
        Button zoomOutButton = findViewById(R.id.zoom_out_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                }
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.zoomOut());
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
                        mMap.moveCamera(cameraUpdate);
                    }
                }
            });
        }

        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                currentLatLng = latLng;
                fetchCurrentWeatherData(latLng, googleMap);
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                fetchPredictedWeatherData(currentLatLng);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentLatLng = marker.getPosition();
                marker.showInfoWindow();
                return false;
            }
        });
    }

    private void fetchCurrentWeatherData(LatLng latLng, GoogleMap googleMap) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&appid=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            float temp = (float) main.getDouble("temp");
                            int truncatedTemp = (int) temp;

                            JSONArray weatherArray = response.getJSONArray("weather");
                            if (weatherArray.length() > 0) {
                                JSONObject weatherObject = weatherArray.getJSONObject(0);
                                String weather = weatherObject.getString("main");
                                String icon = weatherObject.getString("icon");
                                String iconUrl = "https://openweathermap.org/img/wn/" + icon + ".png";

                                // Add a marker with the weather description
                                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(weather).snippet(truncatedTemp + ";" + iconUrl));
                                marker.showInfoWindow();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MapsActivity.this, "Unable to parse weather data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(MapsActivity.this, "Unable to fetch weather data", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void fetchPredictedWeatherData(LatLng latLng){

        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&appid=" + apiKey + "&units=metric";

        List<Forecast> forecasts = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject f3hours = list.getJSONObject(i);

                        Long dt = f3hours.getLong("dt");
                        JSONObject main = f3hours.getJSONObject("main");
                        float temp = (float) main.getDouble("temp");
                        int truncatedTemp = (int) temp;

                        JSONArray weatherArray = f3hours.getJSONArray("weather");
                        String weather = weatherArray.getJSONObject(0).getString("main");
                        String icon = weatherArray.getJSONObject(0).getString("icon");
                        String iconUrl = "https://openweathermap.org/img/wn/" + icon + ".png";

                        // Convert timestamp to string
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        format.setTimeZone(TimeZone.getDefault());
                        String dateString = format.format(new Date(dt * 1000));

                        forecasts.add(new Forecast(dateString, truncatedTemp + "°C", weather, iconUrl));
                    }

                    // Once the data is fetched and stored, display it in the dialog
                    PredictedWeatherDialogFragment dialog = new PredictedWeatherDialogFragment(forecasts);
                    dialog.show(getSupportFragmentManager(), "PredictedWeatherDialogFragment");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}