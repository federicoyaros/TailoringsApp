package com.example.user.tailoringsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 05/09/2015.
 */
public class EditarProyecto extends Activity {

    EditText etNombre, etResponsable, etSocio;
    Spinner spinnerMetodologia;
    Switch switchActivo;
    String nombreProyectoOriginal;
    List<String> listaMetodologias = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_proyecto);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etResponsable = (EditText) findViewById(R.id.etResponsable);
        etSocio = (EditText) findViewById(R.id.etSocio);
        spinnerMetodologia = (Spinner) findViewById(R.id.spinnerMetodologia);
        switchActivo = (Switch) findViewById(R.id.switchActivo);
        nombreProyectoOriginal = getIntent().getExtras().getString("parametro");
        etNombre.setText(nombreProyectoOriginal);

        listaMetodologias.add("Agil");
        listaMetodologias.add("Tradicional");
        listaMetodologias.add("Tradicional con gestion agil");
        listaMetodologias.add("Cliente");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listaMetodologias);
        spinnerMetodologia.setAdapter(adapter);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT * FROM proyecto where nombre = '" +nombreProyectoOriginal + "'" , null);
        fila.moveToFirst();
        etNombre.setText(nombreProyectoOriginal);
        etResponsable.setText(fila.getString(2));
        etSocio.setText(fila.getString(3));
        String metodologia = fila.getString(4);
        int activo = fila.getInt(5);
        if(metodologia.equals("Agil")){
            spinnerMetodologia.setSelection(0);
        }else if(metodologia.equals("Tradicional")){
            spinnerMetodologia.setSelection(1);
        }else if(metodologia.equals("Tradicional con gestion agil")){
            spinnerMetodologia.setSelection(2);
        } else{
            spinnerMetodologia.setSelection(3);
        }
        if(activo == 1){
            switchActivo.setChecked(true);
        }else{
            switchActivo.setChecked(false);
        }

    }
    public void onClickVolverEditarProyecto(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onClickActualizarProyecto(View view){
        if(etNombre.getText().toString().equals("") || etResponsable.getText().toString().equals("") || etSocio.getText().toString().equals("")) {
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
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
                int activo;
                if(switchActivo.isChecked()){
                    activo = 1;
                }else{
                    activo = 0;
                }
                ContentValues registro = new ContentValues();
                registro.put("nombre", nombre);
                registro.put("responsable", responsable);
                registro.put("socio", socio);
                registro.put("metodologia", metodologia);
                registro.put("activo", activo);

                int cant = db.update("proyecto", registro, "nombre='"+nombreProyectoOriginal + "'", null);
                db.close();
                if(cant == 1){
                    Toast.makeText(this, "Se actualizo el proyecto correctamente", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }
            }
        }
    }

    public void onClickEliminarProyecto(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        final SQLiteDatabase db = admin.getWritableDatabase();

        Cursor ids = db.rawQuery("select id from proyecto where nombre = '" + nombreProyectoOriginal + "'", null);
        ids.moveToFirst();
        int proyectoId = ids.getInt(0);
        final Cursor tailorings = db.rawQuery("select id from tailoring where proyecto_id = '" +proyectoId+ "'", null);
        if(tailorings.getCount() > 0){
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("Confirmacion");
            confirmacion.setMessage("El proyecto tiene un tailoring asociado, si lo elimina se eliminara el tailoring tambien");
            confirmacion.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            confirmacion.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tailorings.moveToFirst();
                    do{
                        int tailoringId = tailorings.getInt(0);
                        db.delete("itemsTailoring", "tailoring_id='"+tailoringId + "'", null);
                        db.delete("tailoring", "id='"+tailoringId + "'", null);
                    }while(tailorings.moveToNext());

                    int cant = db.delete("proyecto", "nombre='"+nombreProyectoOriginal + "'", null);
                    db.close();
                    if(cant == 1){
                        Toast.makeText(getApplicationContext(), "Se elimino el proyecto correctamente", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }
            });
            confirmacion.create();
            confirmacion.show();

        }else{

            int cant = db.delete("proyecto", "nombre='"+nombreProyectoOriginal + "'", null);
            db.close();
            if(cant == 1){
                Toast.makeText(this, "Se elimino el proyecto correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
    }
}
