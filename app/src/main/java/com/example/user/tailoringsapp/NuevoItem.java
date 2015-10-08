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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 06/09/2015.
 */
public class NuevoItem extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    EditText etDescripcion, etOpcion;
    Button btnAgregar, btnEliminar, btnCrear, btnVolver;
    List<String> listaOpciones = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_item);

        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        etOpcion = (EditText) findViewById(R.id.etOpcion);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        spinner = (Spinner) findViewById(R.id.spinnerProcesos);

        List<String> listaProcesos = new ArrayList<String>();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("select nombre from proceso", null);
        if(fila.moveToFirst()){
        do{
            listaProcesos.add(fila.getString(0));
        }while(fila.moveToNext());

            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listaProcesos);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaProcesos);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.procesos, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        }

    }

    public void onClickAgregarOpcion(View view){
        if(etOpcion.getText().toString().equals("")) {
            Toast.makeText(this, "Debe completar el nombre de la opcion", Toast.LENGTH_SHORT).show();
        }else{
            listaOpciones.add(etOpcion.getText().toString());
            Toast.makeText(this, "Opcion agregada", Toast.LENGTH_SHORT).show();
            etOpcion.setText("");
        }
    }

    public void onClickEliminarOpcion(View view){

        Toast.makeText(this, "Todavia no hago nada", Toast.LENGTH_SHORT).show();

    }

    public void onClickVolverNuevoItem(View view){
        this.finish();
    }

    public void onClickCrearItem(View view){

        if(etDescripcion.getText().toString().equals("")) {
            Toast.makeText(this, "Debe completar la descripcion del item", Toast.LENGTH_SHORT).show();
        }else if(listaOpciones.size() == 0){
            Toast.makeText(this, "Debe agregar al menos una opcion para el item", Toast.LENGTH_SHORT).show();
        }else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            String descripcion = etDescripcion.getText().toString();
            String nombreProceso = spinner.getSelectedItem().toString();

            Cursor fila = db.rawQuery("select id from proceso where nombre = '" +nombreProceso + "'" , null);
            fila.moveToFirst();

            int proceso_id = fila.getInt(0);

            ContentValues registro = new ContentValues();
            registro.put("descripcion", descripcion);
            registro.put("proceso_id", proceso_id);
            registro.put("habilitado", 1);

            int item_id = (int) db.insert("item", null, registro);

            for (int i = 0; i < listaOpciones.size(); i++ ){
                String opcion = listaOpciones.get(i);
                ContentValues registroOpcion = new ContentValues();
                registroOpcion.put("item_id", item_id);
                registroOpcion.put("opcion", opcion);
                db.insert("opcionesItem", null, registroOpcion);
            }
            db.close();

            Toast.makeText(this, "Item creado correctamente", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
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
