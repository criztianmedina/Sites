package com.example.sites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sites.database.Sitio;
import com.example.sites.database.SitioLab;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{

    public ArrayList<Sitio> listaSitios=new ArrayList<>();

    public RecyclerView lista;

    public RecyclerAdapter adapter;

    private SitioLab sitioLab;

    EditText editTextSearch;
    Button btnSearch;


    //public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sitioLab=new SitioLab(this);

        lista=(RecyclerView) findViewById(R.id.recyclerview);
        lista.setLayoutManager(new LinearLayoutManager(this));
        getAllSitios();
        //para contacto
        adapter = new RecyclerAdapter(this, listaSitios,this);
        lista.setAdapter(adapter);

        EditText editTextSearch = findViewById(R.id.editTextSearch);
        Button btnSearch = findViewById(R.id.btnSearch);

// Configura el listener para el botón de búsqueda
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el texto ingresado en el campo de búsqueda
                String searchText = editTextSearch.getText().toString().trim();

                // Filtra los sitios en función del texto de búsqueda
                filterSites(searchText);
            }
        });

    }

    private void filterSites(String searchText) {
        // Crea una nueva lista para almacenar los sitios filtrados
        ArrayList<Sitio> filteredSites = new ArrayList<>();

        // Recorre la lista de sitios existente
        for (Sitio sitio : listaSitios) {
            // Si el título del sitio contiene el texto de búsqueda (ignorando mayúsculas/minúsculas), agrégalo a la lista filtrada
            if (sitio.getTitulo().toLowerCase().contains(searchText.toLowerCase())) {
                filteredSites.add(sitio);
            }
        }

        // Actualiza el adaptador del RecyclerView con la lista filtrada
        adapter = new RecyclerAdapter(MainActivity.this, filteredSites, MainActivity.this);
        lista.setAdapter(adapter);
    }

    //para contato
    @Override
    public void onItemClick(int position) {
        if (position >= 0 && position < listaSitios.size()) {
            Sitio sitioSeleccionado = listaSitios.get(position);

            // Crear un Intent para pasar a la segunda actividad
            Intent intent = new Intent(MainActivity.this, DetallesActivity.class);
            intent.putExtra("id", sitioSeleccionado.getId());

            // Puedes pasar datos adicionales a la segunda actividad usando el Intent
            intent.putExtra("TITULO", sitioSeleccionado.getTitulo());
            intent.putExtra("NOMBRE", sitioSeleccionado.getNombre());
            intent.putExtra("TELEFONO", sitioSeleccionado.getTelefono());
            intent.putExtra("DESCRIPCION", sitioSeleccionado.getDescripcion());
            intent.putExtra("LATITUD", sitioSeleccionado.getLatitud());
            intent.putExtra("LONGITUD", sitioSeleccionado.getLongitud());
            intent.putExtra("IMAGEN", sitioSeleccionado.getFoto());

            // Iniciar la segunda actividad
            startActivity(intent);
        }
    }


    public void getAllSitios(){
        listaSitios.clear();
        listaSitios.addAll(sitioLab.getSitios());

    }

    @Override
    protected void onResume(){
        super.onResume();

        getAllSitios();
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.agregarsitio){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,
                    FormularioActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}