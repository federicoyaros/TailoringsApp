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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 23/09/2015.
 */
public class ItemTailoring extends Activity implements CompoundButton.OnCheckedChangeListener{

    Switch switchNoAplica;
    EditText etItem;
    Spinner spinnerTemplate, spinnerResponsable, spinnerFase;
    Button btnSeleccionar, btnSiguiente;
    ProgressBar progressBar;
    int tailoringId, cantidadItems, itemId;

    List<String> listaTemplate = new ArrayList<String>();
    List<String> listaResponsable = new ArrayList<String>();
    List<String> listaFase = new ArrayList<String>();

    //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
    Cursor items, opciones;
    SQLiteDatabase db;

    final ArrayList<CharSequence> listaOpcionesElegidas = new ArrayList<CharSequence>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_tailoring);

        switchNoAplica = (Switch) findViewById(R.id.switchNoAplica);
        etItem = (EditText) findViewById(R.id.etItem);
        spinnerTemplate = (Spinner) findViewById(R.id.spinnerTemplate);
        spinnerResponsable = (Spinner) findViewById(R.id.spinnerResponsable);
        spinnerFase = (Spinner) findViewById(R.id.spinnerFase);
        btnSeleccionar = (Button) findViewById(R.id.btnSeleccionar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);

        tailoringId = getIntent().getExtras().getInt("tailoring_id");

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        db = admin.getWritableDatabase();
        items = db.rawQuery("select id, descripcion from item", null);
        cantidadItems = items.getCount();
        progressBar.setMax(cantidadItems);
        progressBar.setProgress(1);
        items.moveToFirst();
        items.moveToNext();
        itemId = items.getInt(0);
        etItem.setText(items.getString(1));

        opciones = db.rawQuery("select opcion from opcionesItem where item_id = '" +itemId+ "'", null);

        listaTemplate.add("Nosotros");
        listaTemplate.add("Cliente");
        listaTemplate.add("Proyecto");

        listaResponsable.add("Nosotros");
        listaResponsable.add("Cliente");
        listaResponsable.add("Ambos");

        listaFase.add("Inicio / Elaboracion");
        listaFase.add("Desarrollo / Construccion");
        listaFase.add("Fin / Transicion");
        listaFase.add("Durante todo el proyecto");

        ArrayAdapter adapterTemplate = new ArrayAdapter<String>(this, R.layout.spinner_item, listaTemplate);
        spinnerTemplate.setAdapter(adapterTemplate);
        ArrayAdapter adapterResponsable = new ArrayAdapter<String>(this, R.layout.spinner_item, listaResponsable);
        spinnerResponsable.setAdapter(adapterResponsable);
        ArrayAdapter adapterFase = new ArrayAdapter<String>(this, R.layout.spinner_item, listaFase);
        spinnerFase.setAdapter(adapterFase);

        switchNoAplica.setOnCheckedChangeListener(this);

    }

    protected void mostrarOpciones() {

        final ArrayList<CharSequence> listaOpciones = new ArrayList<CharSequence>();
        //final ArrayList<CharSequence> listaOpcionesElegidas = new ArrayList<CharSequence>();
        //List<String> listaOpciones = new ArrayList<String>();
        //List<String> listaOpcionesElegidas = new ArrayList<String>();

        boolean[] opcionesElegidas = new boolean[opciones.getCount()];
        int cantidad = opciones.getCount();

        if(opciones.moveToFirst()){
            do{
                listaOpciones.add(opciones.getString(0));
            }while(opciones.moveToNext());
        }

        for(int i = 0; i < cantidad; i++) opcionesElegidas[i] = listaOpcionesElegidas.contains(listaOpciones.get(i));

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                    listaOpcionesElegidas.add(listaOpciones.get(which));
                else
                    listaOpcionesElegidas.remove(listaOpciones.get(which));
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione:");
        final CharSequence[] listaOpcionesMostrar = listaOpciones.toArray(new CharSequence[listaOpciones.size()]);
        builder.setMultiChoiceItems(listaOpcionesMostrar, opcionesElegidas, coloursDialogListener);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void onClickA(View v) {

        mostrarOpciones();

    }

    public void onClickSiguiente(View v) {


        if(switchNoAplica.isChecked()){
            ContentValues itemTailoringNoAplica = new ContentValues();
            itemTailoringNoAplica.put("tailoring_id", tailoringId);
            itemTailoringNoAplica.put("item_id", itemId);
            itemTailoringNoAplica.put("no_aplica", 1);
            db.insert("itemsTailoring", null, itemTailoringNoAplica);
        }else{

            for(int i = 0; i < listaOpcionesElegidas.size(); i++){
                ContentValues itemTailoring = new ContentValues();
                itemTailoring.put("tailoring_id", tailoringId);
                itemTailoring.put("item_id", itemId);
                String opcionElegida = listaOpcionesElegidas.get(i).toString();
                Cursor opcionesElegidas = db.rawQuery("select id from opcionesItem where item_id = '" +itemId+ "' and opcion = '"+opcionElegida+"'", null);
                opcionesElegidas.moveToFirst();
                int opcionElegidaId = opcionesElegidas.getInt(0);
                itemTailoring.put("opcion_elegida_id", opcionElegidaId);
                db.insert("itemsTailoring", null, itemTailoring);
            }
        }

        if(items.moveToNext()){
            if(switchNoAplica.isChecked()){
                switchNoAplica.setChecked(false);
            }
            progressBar.incrementProgressBy(1);
            itemId = items.getInt(0);
            etItem.setText(items.getString(1));
            opciones = db.rawQuery("select opcion from opcionesItem where item_id = '" +itemId+ "'", null);
            listaOpcionesElegidas.clear();
            spinnerTemplate.setSelection(0);
            spinnerResponsable.setSelection(0);
            spinnerFase.setSelection(0);

        } else{
            ContentValues tailoring = new ContentValues();
            tailoring.put("completo", 1);
            int cant = db.update("tailoring", tailoring, "id='"+tailoringId + "'", null);
            db.close();
            if(cant==1){
                Toast.makeText(this, "Tailoring creado correctamente", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){
            spinnerTemplate.setEnabled(false);
            spinnerResponsable.setEnabled(false);
            spinnerFase.setEnabled(false);
            btnSeleccionar.setEnabled(false);
        }else{
            spinnerTemplate.setEnabled(true);
            spinnerResponsable.setEnabled(true);
            spinnerFase.setEnabled(true);
            btnSeleccionar.setEnabled(true);
        }
    }
}
