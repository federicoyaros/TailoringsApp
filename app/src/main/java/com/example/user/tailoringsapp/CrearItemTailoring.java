package com.example.user.tailoringsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by User on 17/09/2015.
 */
public class CrearItemTailoring extends Activity implements View.OnClickListener {

    protected Button selectColoursButton;

    protected CharSequence[] colours = { "Red", "Green", "Blue" };

    protected ArrayList<CharSequence> selectedColours = new ArrayList<CharSequence>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.crear_item_tailoring);

        selectColoursButton = (Button) findViewById(R.id.select_colours);

        selectColoursButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.select_colours:

                showSelectColoursDialog();

                break;

            default:

                break;

        }

    }

    protected void showSelectColoursDialog() {

        boolean[] checkedColours = new boolean[colours.length];

        int count = colours.length;

        for(int i = 0; i < count; i++)

            checkedColours[i] = selectedColours.contains(colours[i]);

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)

                    selectedColours.add(colours[which]);

                else

                    selectedColours.remove(colours[which]);

                onChangeSelectedColours();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Colours");

        builder.setMultiChoiceItems(colours, checkedColours, coloursDialogListener);

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

    protected void onChangeSelectedColours() {

        StringBuilder stringBuilder = new StringBuilder();

        for(CharSequence colour : selectedColours)

            stringBuilder.append(colour + ",");

        selectColoursButton.setText(stringBuilder.toString());

    }
}
