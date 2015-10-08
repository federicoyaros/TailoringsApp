package com.example.user.tailoringsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by User on 08/10/2015.
 */
public class EditarTailoring extends Activity {

    EditText etProyecto, etResponsableMetodologia, etResponsableProyecto, etFechaCreacion, etFechaModificado;
    Button btnEditar, btnEliminar, btnVolver;
    int tailoringId;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_tailoring);

        tailoringId = getIntent().getExtras().getInt("parametro");

        etProyecto = (EditText) findViewById(R.id.etProyecto);
        etResponsableMetodologia = (EditText) findViewById(R.id.etResponsableMetodologia);
        etResponsableProyecto = (EditText) findViewById(R.id.etResponsableProyecto);
        etFechaCreacion = (EditText) findViewById(R.id.etFechaCreacion);
        etFechaModificado = (EditText) findViewById(R.id.etFechaModificado);

        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnVolver = (Button) findViewById(R.id.btnVolver);

        etProyecto.setEnabled(false);
        etResponsableMetodologia.setEnabled(false);
        etResponsableProyecto.setEnabled(false);
        etFechaCreacion.setEnabled(false);
        etFechaModificado.setEnabled(false);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        db = admin.getWritableDatabase();
        Cursor tailoring = db.rawQuery("SELECT * FROM tailoring where id = '" + tailoringId + "'", null);
        tailoring.moveToFirst();
        Cursor proyecto = db.rawQuery("SELECT nombre FROM proyecto where id = '" +tailoring.getInt(1) + "'" , null);
        proyecto.moveToFirst();

        etProyecto.setText(proyecto.getString(0));
        etResponsableMetodologia.setText(tailoring.getString(2));
        etResponsableProyecto.setText(tailoring.getString(3));
        etFechaCreacion.setText(tailoring.getString(4));
        etFechaModificado.setText(tailoring.getString(5));


    }

    public void onClickVolverEditarTailoring(View view){

        this.finish();

    }

    public void onClickEliminarEditarTailoring(View view){

        AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
        confirmacion.setTitle("Confirmacion");
        confirmacion.setMessage("\u00BFEsta seguro que desea eliminar el tailoring?");
        confirmacion.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        confirmacion.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete("itemsTailoring", "tailoring_id='" + tailoringId + "'", null);
                db.delete("tailoring", "id='" + tailoringId + "'", null);
                Toast.makeText(getApplicationContext(), "Se elimino el tailoring correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        confirmacion.create();
        confirmacion.show();

    }
}
