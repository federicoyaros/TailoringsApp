package com.example.user.tailoringsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener{

    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tab = actionBar.newTab().setText("Proyectos").setTabListener(this);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("Tailorings").setTabListener(this);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("Items").setTabListener(this);
        actionBar.addTab(tab);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        if (isFirstTime()) {

        ContentValues registro = new ContentValues();
        registro.put("nombre", "Gestion de proyectos de alto nivel");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Gestion de requerimientos");
        db.insert("proceso", null, registro);
        /*registro.put("nombre", "Gestion de proyectos tradicional");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Gestion de requerimientos");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Disenio tecnico");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Disenio grafico");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Implementacion");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Testing");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Evaluacion de alternativas");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Revisiones por pares");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Despliegue");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Medicion y analisis");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Administracion de riesgos");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Aseguramiento de calidad");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Gestion de casos de cliente");
        db.insert("proceso", null, registro);
        registro.put("nombre", "Plan de carrera");
        db.insert("proceso", null, registro);*/

        ContentValues item = new ContentValues();
        item.put("descripcion", "informe al socio");
        item.put("proceso_id", 1);
        item.put("habilitado", 1);
        db.insert("item", null, item);
        item.put("descripcion", "User stories");
        item.put("proceso_id", 2);
        item.put("habilitado", 1);
        db.insert("item", null, item);


        db.close();
    }



    }

    public class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }
        public android.support.v4.app.Fragment getItem(int arg0){
            switch(arg0){
                case 0:
                    return new Fragment_Proyectos();
                case 1:
                    return new Fragment_Tailorings();
                case 2:
                    return new Fragment_Items();
                default:
                    return null;
            }
        }
        public int getCount(){
            return 3;
        }

    }



    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        getSupportActionBar().setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public void onClickNuevoProyecto(View view){

        Intent i = new Intent(this, NuevoProyecto.class);
        startActivity(i);

    }

    public void onClickNuevoItem(View view){

        Intent i = new Intent(this, NuevoItem.class);
        startActivity(i);

    }

    public void onClickNuevoTailoring(View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor proyectos = db.rawQuery("select id from proyecto", null);

        if(proyectos.getCount() == 0){

            Toast.makeText(this, "No hay proyectos disponibles", Toast.LENGTH_SHORT).show();

        }else{

            Intent i = new Intent(this, NuevoTailoring.class);
            startActivity(i);
        }

    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }


}
