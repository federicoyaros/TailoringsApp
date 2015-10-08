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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 05/09/2015.
 */
public class Fragment_Tailorings extends Fragment {
    View rootView;
    ListView listViewTailorings;
    TextView txtNoHayTailorings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_tailorings, container, false);

        listViewTailorings = (ListView) rootView.findViewById(R.id.listViewTailorings);
        txtNoHayTailorings = (TextView) rootView.findViewById(R.id.txtNoHayTailorings);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        //final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        final List<String> listaTailorings = new ArrayList<String>();
        final List<String> listaTailoringsFechas = new ArrayList<String>();
        final List<Integer> listaTailoringsId = new ArrayList<Integer>();
        Cursor tailoringsProyectosId = db.rawQuery("select id, proyecto_id, fecha_ultima_modificacion from tailoring", null);

        if(tailoringsProyectosId.getCount() == 0){
            txtNoHayTailorings.setVisibility(rootView.VISIBLE);
        }else {
            txtNoHayTailorings.setVisibility(rootView.INVISIBLE);
            tailoringsProyectosId.moveToFirst();
            do {
                listaTailoringsId.add(tailoringsProyectosId.getInt(0));
                Cursor tailoringsProyectosNombre = db.rawQuery("select nombre from proyecto where id = '" + tailoringsProyectosId.getInt(1) + "'", null);
                tailoringsProyectosNombre.moveToFirst();
                listaTailorings.add(tailoringsProyectosNombre.getString(0));
                listaTailoringsFechas.add(tailoringsProyectosId.getString(2));
            } while (tailoringsProyectosId.moveToNext());

            //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, listaTailorings) {
            final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, listaTailoringsId) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(listaTailorings.get(position));
                    text2.setText("Modificado: " + listaTailoringsFechas.get(position));

                    return view;
                }
            };
            listViewTailorings.setAdapter(adapter);
            listViewTailorings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int tailoringId = (int) adapter.getItem(position);
                    Intent i = new Intent(getActivity(), EditarTailoring.class);
                    i.putExtra("parametro", tailoringId);
                    startActivity(i);

                }
            });
        }
        return rootView;
    }
}



