package com.example.user.tailoringsapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 05/09/2015.
 */
public class Fragment_Items extends Fragment {
    View rootView;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_items, container, false);


        expListView = (ExpandableListView) rootView.findViewById(R.id.expLV);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "baseDeDatos58", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("select id, nombre from proceso", null);
        int i = 0;
        fila.moveToFirst();
        do{
            listDataHeader.add(fila.getString(1));
            int proceso_id = fila.getInt(0);
            Cursor itemsProceso = db.rawQuery("select descripcion from item where proceso_id = '" +proceso_id + "'", null);
            itemsProceso.moveToFirst();
            List<String> items = new ArrayList<String>();
            do{
                items.add(itemsProceso.getString(0));
            }while(itemsProceso.moveToNext());
            listDataChild.put(listDataHeader.get(i), items);
            i++;
        }while(fila.moveToNext());



        // Adding child data
       // listDataHeader.add("Top 250");
        //listDataHeader.add("Now Showing");
        //listDataHeader.add("Coming Soon..");
/*
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();

        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");
*/
        //listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        //listDataChild.put(listDataHeader.get(1), nowShowing);
        //listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}

