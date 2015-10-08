package com.example.user.tailoringsapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 05/09/2015.
 */
public class NuevoProyecto extends Activity {

    EditText etNombre, etResponsable, etSocio;
    Spinner spinnerMetodologia;
    List<String> listaMetodologias = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_proyecto);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etResponsable = (EditText) findViewById(R.id.etResponsable);
        etSocio = (EditText) findViewById(R.id.etSocio);
        spinnerMetodologia = (Spinner) findViewById(R.id.spinnerMetodologia);

        listaMetodologias.add("Agil");
        listaMetodologias.add("Tradicional");
        listaMetodologias.add("Tradicional con gestion agil");
        listaMetodologias.add("Cliente");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listaMetodologias);
        spinnerMetodologia.setAdapter(adapter);


    }
    public void onClickVolverNuevoProyecto(View view){

        this.finish();
    }

    public void onClickCrearProyecto(View view){

        if(etNombre.getText().toString().equals("") || etResponsable.getText().toString().equals("") || etSocio.getText().toString().equals("")){
            Toast.makeText(this,"Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            String nombre = etNombre.getText().toString();

            Cursor proyectoMismoNombre = db.rawQuery("select id from proyecto where nombre = '" + nombre + "'", null);
            if(proyectoMismoNombre.getCount() > 0){
                etNombre.setText("");
                Toast.makeText(this,"Ya existe un proyecto con ese nombre", Toast.LENGTH_SHORT).show();
            }else{
                String responsable = etResponsable.getText().toString();
                String socio = etSocio.getText().toString();
                String metodologia = spinnerMetodologia.getSelectedItem().toString();
                int activo = 1;
                ContentValues registro = new ContentValues();
                registro.put("nombre", nombre);
                registro.put("responsable", responsable);
                registro.put("socio", socio);
                registro.put("metodologia", metodologia);
                registro.put("activo", 1);
                db.insert("proyecto", null, registro);
                db.close();
                Toast.makeText(this,"Proyecto creado correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
    }
}

