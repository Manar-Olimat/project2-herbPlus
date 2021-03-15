package com.example.project_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class add_plant extends AppCompatActivity {
    TextView symptoms;
    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray={"Fatigue","Fever","Stomachache","Headache","Nausea","Skin irritation",
            "Indigestion","Infections and ulcers","Diarrhea","Constipation","Colds"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        symptoms = findViewById(R.id.symptoms);
        selectedsymptoms = new boolean[symptomsArray.length];

        symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_plant.this);
                builder.setTitle("Selected Symptoms");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(symptomsArray, selectedsymptoms, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            symptomsList.add(which);
                            Collections.sort(symptomsList);
                        }
                        else if(symptomsList.contains(which)){
                            symptomsList.remove(Integer.valueOf(which));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < symptomsList.size(); i++) {
                            stringBuilder.append(symptomsArray[symptomsList.get(i)]);
                            if (i != symptomsList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        symptoms.setText(stringBuilder.toString());

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<selectedsymptoms.length;i++){
                            selectedsymptoms[i]=false;
                            symptomsList.clear();
                            symptoms.setText("");
                        }
                    }
                }).show();
            }
        });


    }
}