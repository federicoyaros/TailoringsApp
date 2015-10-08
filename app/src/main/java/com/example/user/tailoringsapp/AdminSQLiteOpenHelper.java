package com.example.user.tailoringsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 05/09/2015.
 */
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {


    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table proyecto(id integer primary key autoincrement, nombre text, responsable text, socio text, metodologia text, activo integer)");
        db.execSQL("create table proceso(id integer primary key autoincrement, nombre text)");
        db.execSQL("create table item(id integer primary key autoincrement, descripcion text, proceso_id integer, habilitado integer, FOREIGN KEY(proceso_id) REFERENCES proceso(id))");
        db.execSQL("create table opcionesItem(id integer primary key autoincrement, item_id integer, opcion text, foreign key(item_id) references item(id))");
        db.execSQL("create table tailoring(id integer primary key autoincrement, proyecto_id integer, responsable_metodologia text, responsable_proyecto text, fecha_creacion text, fecha_ultima_modificacion text, completo integer, foreign key(proyecto_id) references proyecto(id))");
        db.execSQL("create table itemsTailoring(id integer primary key autoincrement, tailoring_id integer, item_id integer, opcion_elegida_id integer, no_aplica integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
