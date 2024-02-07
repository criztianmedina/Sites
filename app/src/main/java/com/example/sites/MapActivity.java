package com.example.sites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;
    private Button btnmap;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        permiso();

        btnmap = findViewById(R.id.btncarro);
        btnmap.setOnClickListener(this);
        miPosicion();

        Button showLocationButton = findViewById(R.id.showLocationButton);
        showLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asegurarnos de que miPosicion() se haya ejecutado antes de intentar obtener currentLocation


                // Obtener la latitud y longitud actual
                double latitud = currentLocation != null ? currentLocation.latitude : 0.0;
                double longitud = currentLocation != null ? currentLocation.longitude : 0.0;

                // Crear un Intent para pasar la información a FormularioActivity
                Intent intent = new Intent(MapActivity.this, FormularioActivity.class);
                intent.putExtra("LATITUD", latitud);
                intent.putExtra("LONGITUD", longitud);
                startActivity(intent);

                // Llamar al método para mostrar la ubicación en FormularioActivity
                showLocationInFormulario();
            }
        });
    }

    private void showLocationInFormulario() {
        Intent intent = new Intent(MapActivity.this, FormularioActivity.class);
        intent.putExtra("LATITUD", currentLocation != null ? currentLocation.latitude : 0.0);
        intent.putExtra("LONGITUD", currentLocation != null ? currentLocation.longitude : 0.0);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        Intent intent = getIntent();
        double latitud = intent.getDoubleExtra("LATITUD", 0.0);
        double longitud = intent.getDoubleExtra("LONGITUD", 0.0);

        // Agregar un marcador en la ubicación recibida
        LatLng location = new LatLng(latitud, longitud);
        addMarkerToMap(location);
    }

    private void miPosicion() {
        LocationManager objLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener objLocListener = new miposicion();

        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }

        objLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, objLocListener);

        if (objLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // No realices ninguna acción relacionada con la ubicación aquí.
            // Espera a que se invoque onLocationChanged().
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
            alert.setTitle("GPS no está activado");
            alert.setMessage("Conectando con GPS");
            alert.setPositiveButton("ok", null);
            alert.create().show();
        }
    }


    public void permiso() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnmap) {
            miPosicion();
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        addMarkerToMap(latLng);
        Toast.makeText(MapActivity.this, "click en " + latLng, Toast.LENGTH_SHORT).show();
        // Depuración
        Log.d("MapActivity", "Click largo en " + latLng);
    }

    private void addMarkerToMap(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Ubicación Guardada"));
        currentLocation = latLng;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
    }
}