package com.example.user.tailoringsapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 05/09/2015.
 */
public class Fragment_Proyectos extends Fragment {
    View rootView;
    ListView listViewProyectos;
    TextView txtNoHayProyectos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_proyectos2, container, false);

        listViewProyectos = (ListView) rootView.findViewById(R.id.listViewProyectos);
        txtNoHayProyectos = (TextView) rootView.findViewById(R.id.txtNoHayProyectos);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        Cursor fila = db.rawQuery("select nombre from proyecto", null);

        if(fila.getCount() == 0){
            txtNoHayProyectos.setVisibility(rootView.VISIBLE);
        }else {
            txtNoHayProyectos.setVisibility(rootView.INVISIBLE);
            fila.moveToFirst();
            do {
                adaptador.add(fila.getString(0));
            } while (fila.moveToNext());

            listViewProyectos.setAdapter(adaptador);
            listViewProyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String proyecto = adaptador.getItem(position);
                    Intent i = new Intent(getActivity(), EditarProyecto.class);
                    i.putExtra("parametro", proyecto);
                    startActivity(i);

                }
            });
        }


        return rootView;
    }

}
