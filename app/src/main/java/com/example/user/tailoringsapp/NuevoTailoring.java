package com.example.user.tailoringsapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 15/09/2015.
 */
public class NuevoTailoring extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner spinnerProyectos;
    EditText etResponsableMetodologia, etResponsableProyecto;
    Button btnCrear, btnVolver;

    //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
    //SQLiteDatabase db = admin.getWritableDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_tailoring);

        spinnerProyectos = (Spinner) findViewById(R.id.spinnerProyectos);
        etResponsableMetodologia = (EditText) findViewById(R.id.etResponsableMetodologia);
        etResponsableProyecto = (EditText) findViewById(R.id.etResponsableProyecto);
        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnVolver = (Button) findViewById(R.id.btnVolver);

        List<String> listaProyectos = new ArrayList<String>();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor proyectos = db.rawQuery("select nombre from proyecto", null);
        proyectos.moveToFirst();
            do {
                listaProyectos.add(proyectos.getString(0));
            } while (proyectos.moveToNext());

            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listaProyectos);
            spinnerProyectos.setAdapter(adapter);
            spinnerProyectos.setOnItemSelectedListener(this);

        }

    public void onClickVolverNuevoTailoring(View view){

        this.finish();
    }

    public void onClickCrearTailoring(View view){

        if(etResponsableMetodologia.getText().toString().equals("") || etResponsableProyecto.getText().toString().equals("")){
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        }else{

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            String proyecto = spinnerProyectos.getSelectedItem().toString();
            Cursor proyectos_id = db.rawQuery("select id from proyecto where nombre = '" + proyecto + "'", null);
            proyectos_id.moveToFirst();
            int proyecto_id = proyectos_id.getInt(0);
            String responsableMetodologia = etResponsableMetodologia.getText().toString();
            String responsableProyecto = etResponsableProyecto.getText().toString();

            Calendar calendario = Calendar.getInstance();
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int anio = calendario.get(Calendar.YEAR);

            String fecha = new StringBuilder()
                    .append(anio).append(" ")
                    .append("-").append(mes + 1).append(" ")
                    .append("-").append(" ")
                    .append(dia)
                    .toString();

            ContentValues tailoring = new ContentValues();
            tailoring.put("proyecto_id", proyecto_id);
            tailoring.put("responsable_metodologia", responsableMetodologia);
            tailoring.put("responsable_proyecto", responsableProyecto);
            tailoring.put("fecha_creacion", fecha);
            tailoring.put("fecha_ultima_modificacion", fecha);
            tailoring.put("completo", 0);
            int tailoringId = (int) db.insert("tailoring", null, tailoring);
            db.close();

            Intent i = new Intent(this, ItemTailoring.class);
            i.putExtra("tailoring_id", tailoringId);
            startActivity(i);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
