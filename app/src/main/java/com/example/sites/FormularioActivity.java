package com.example.sites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sites.database.Sitio;
import com.example.sites.database.SitioLab;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class FormularioActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextTitulo,editTextNombre,editTextTelefono,editTextDescripcion,editTextLatitud,editTextLongitud;
    private TextView textViewLatitud,textViewLongitud;

    private boolean usuarioDecideGuardar = false;
    private SitioLab sitioLab;
    Button btnguardar,btncamara;
    ImageView imgview;
    private byte[] fotoData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        btnguardar = findViewById(R.id.buttonGuardar);
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);

        editTextLatitud = findViewById(R.id.editTexLatitud);
        editTextLongitud = findViewById(R.id.editTexLongitud);

        btncamara = findViewById(R.id.buttonagregarfoto);
        imgview = findViewById(R.id.imageViewPerfil);

        btncamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrircamara();
            }
        });

        // Recuperar valores de latitud y longitud desde el Intent
        Intent intent = getIntent();
        if (intent != null) {
            double latitud = intent.getDoubleExtra("LATITUD", 0.0);
            double longitud = intent.getDoubleExtra("LONGITUD", 0.0);

            // Mostrar valores en TextViews
            editTextLatitud.setText(String.valueOf(latitud));
            editTextLongitud.setText(String.valueOf(longitud));
        }

        sitioLab = new SitioLab(this);

        btnguardar.setOnClickListener(this);

        Button buttonagregarubicacion = findViewById(R.id.buttonagregarubicacion);
        buttonagregarubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la Activity del mapa
                Intent intent = new Intent(FormularioActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void abrircamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            imgview.setImageBitmap(imgBitmap);

            // Convertir la imagen a byte[]
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            fotoData = stream.toByteArray();
        }
    }


    public void onClick(View view) {
        if (view == btnguardar) {
            // Verificar que no haya campos en blanco
            if (camposNoEstanEnBlanco()) {
                // Si todos los campos están completos, proceder con la inserción y mostrar cuadro de diálogo
                mensajeconfirmacion();
                getAllPersonas();

            } else {
                // Si hay campos en blanco, mostrar un mensaje al usuario o realizar otra acción apropiada
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean camposNoEstanEnBlanco() {
        // Verificar que no haya campos en blanco
        return !obtenerTexto(editTextTitulo).trim().isEmpty() &&
                !obtenerTexto(editTextNombre).trim().isEmpty() &&
                !obtenerTexto(editTextDescripcion).trim().isEmpty() &&
                !obtenerTexto(editTextTelefono).trim().isEmpty() &&
                !obtenerTexto(editTextLatitud).trim().isEmpty()
                && !obtenerTexto(editTextLongitud).trim().isEmpty();
    }


    private String obtenerTexto(EditText editText) {
        return editText.getText().toString();
    }


    private void mensajeconfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Guardar cambios?");
        builder.setMessage("¿Estás seguro de que quieres guardar los cambios?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Si el usuario elige 'Sí'
                usuarioDecideGuardar = true;
                showToast("Guardado correctamente. ¡Sí!");
                // Guardar los cambios solo si el usuario elige 'Sí'
                if (usuarioDecideGuardar) {
                    insertSitios();
                    getAllPersonas();
                    // Solo iniciar MainActivity si se guardan los cambios
                    Intent intent = new Intent(FormularioActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Si el usuario elige 'No', no guardar los cambios
                usuarioDecideGuardar = false;
                showToast("No se guardaron cambios.");
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void insertSitios() {
        // Obtener datos del formulario
        String titulo = editTextTitulo.getText().toString();
        String nombre = editTextNombre.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();

        double latitud = Double.parseDouble(editTextLatitud.getText().toString());
        double longitud = Double.parseDouble(editTextLongitud.getText().toString());
        //String latitud = editTextLatitud.getText().toString();
        //String longitud = editTextLongitud.getText().toString();


        // Crear una instancia de Persona con los datos del formulario y la imagen en byte[]
        Sitio sitio = new Sitio();
        sitio.setFoto(fotoData);
        sitio.setTitulo(titulo);
        sitio.setNombre(nombre);
        sitio.setTelefono(telefono);
        sitio.setDescripcion(descripcion);
        sitio.setLatitud(latitud);
        sitio.setLongitud(longitud);

        // Agregar la nueva persona a la base de datos utilizando PersonaLab
        sitioLab.addSitio(sitio);
    }

    public void getAllPersonas() {
        SitioLab sitioLab = SitioLab.get(getApplicationContext());
        List<Sitio> sitios = sitioLab.getSitios();
        for (Sitio p : sitios) {
            // Log u otra forma de mostrar los datos
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}