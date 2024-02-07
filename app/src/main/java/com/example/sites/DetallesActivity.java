package com.example.sites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sites.database.Sitio;
import com.example.sites.database.SitioLab;

public class DetallesActivity extends AppCompatActivity {

    SitioLab sitioLab ;
    Sitio sitio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        sitioLab = new SitioLab(this);
        Bundle extras = getIntent().getExtras();

        int id = extras.getInt("id", -1);

        sitio = new Sitio();
        try {
            sitio = sitioLab.getSitio(String.valueOf(id));
            Toast.makeText(this, sitio.getTitulo(), Toast.LENGTH_SHORT).show();

            String titulo = sitio.getTitulo();
            String nombre = sitio.getNombre();
            String telefono = sitio.getTelefono();
            String descripcion = sitio.getDescripcion();
            Double latitud = sitio.getLatitud();
            Double longitud = sitio.getLongitud();
            byte[] imagen = sitio.getFoto();


            TextView textViewTitulo = findViewById(R.id.textViewTitulo);
            TextView textViewNombre = findViewById(R.id.textViewNombre);
            TextView textViewTelefono = findViewById(R.id.textViewTelefono);
            TextView textViewDescripcion = findViewById(R.id.textViewDescripcion);
            TextView textViewLatitud = findViewById(R.id.textViewLatitud);
            TextView textViewLongitud = findViewById(R.id.textViewLongitud);
            ImageView imageViewPerfil = findViewById(R.id.imageViewPerfil);

            textViewTitulo.setText(titulo);
            textViewNombre.setText(nombre);
            textViewTelefono.setText(telefono);
            textViewDescripcion.setText(descripcion);
            textViewLatitud.setText(String.valueOf(latitud));
            textViewLongitud.setText(String.valueOf(longitud));
            if (imagen != null) {
                // Si la imagen no es nula, cargar la imagen del sitio
                Glide.with(this)
                        .load(imagen)
                        .into(imageViewPerfil);
            } else {
                // Si la imagen es nula, cargar la imagen predeterminada
                Glide.with(this)
                        .load(R.drawable.mont)
                        .into(imageViewPerfil);
            }


            ImageButton buttonEliminar = findViewById(R.id.btnEliminar);
            buttonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eliminarSitio();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Declarar e inicializar el botón dentro del contexto de onCreate
        Button buttonVerUbicacion = findViewById(R.id.btnVerUbicacion);
        buttonVerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verUbicacion();
            }
        });
    }

    private void verUbicacion() {
        // Obtener las coordenadas almacenadas en la base de datos
        double latitud = sitio.getLatitud();
        double longitud = sitio.getLongitud();

        // Crear un Intent para abrir la MapActivity
        Intent intent = new Intent(DetallesActivity.this, MapActivity.class);
        intent.putExtra("LATITUD", latitud);
        intent.putExtra("LONGITUD", longitud);
        startActivity(intent);
    }

    private void eliminarSitio() {
        // Eliminar el sitio de la base de datos
        sitioLab.deleteSitio(sitio);
        Toast.makeText(this, "Sitio eliminado", Toast.LENGTH_SHORT).show();
        // Terminar la actividad después de eliminar el sitio
        finish();
    }
}