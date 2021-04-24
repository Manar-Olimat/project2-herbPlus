package com.example.project_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class update_plant extends AppCompatActivity {
    MaterialToolbar back;
    TextView plantname, symptoms;
    MultiAutoCompleteTextView description, information;
    Button updateplant;
    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray={"Fatigue","Fever","Stomachache","Headache","Nausea","Skin irritation",
            "Indigestion","Infections and ulcers","Diarrhea","Constipation","Colds"};


    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plant);
        plantname =findViewById(R.id.plantname);
        symptoms = findViewById(R.id.symptoms);
        description=findViewById(R.id.description);
        information=findViewById(R.id.information);
        updateplant=findViewById(R.id.updateplant);
        selectedsymptoms = new boolean[symptomsArray.length];
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        String name1 = prefs.getString("name", "No name defined");
        String Symptoms = prefs.getString("Symptoms", "No Symptoms defined");
        String description1 = prefs.getString("description", "No description defined");
        String information1 = prefs.getString("information", "No information defined");

        plantname.setText(name1);
        symptoms.setText(Symptoms);
        description.setText(description1);
        information.setText(information1);
        back=findViewById(R.id.topAppBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(update_plant.this, view_plant.class));

            }
        });
        symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(update_plant.this);
                builder.setTitle("Selected Symptoms...");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(symptomsArray, selectedsymptoms, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            symptomsList.add(which);
                            Collections.sort(symptomsList);
                        } else if (symptomsList.contains(which)) {
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
                        symptoms.setError(null);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedsymptoms.length; i++) {
                            selectedsymptoms[i] = false;
                            symptomsList.clear();
                            symptoms.setText("");
                        }
                    }
                }).show();
            }
        });
        updateplant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !symptoms.getText().toString().isEmpty() && !description.getText().toString().isEmpty()
                        &&!information.getText().toString().isEmpty()  ){

                reference = FirebaseDatabase.getInstance().getReference("plants");
                reference.child(name1).child("symptoms").setValue(symptoms.getText().toString());
                reference.child(name1).child("description").setValue(description.getText().toString());
                reference.child(name1).child("information").setValue(information.getText().toString());

                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("viewplant", MODE_PRIVATE).edit();
                editor.putString("name", name1);
                editor.putString("Symptoms", symptoms.getText().toString());
                editor.putString("description",description.getText().toString());
                editor.putString("information", information.getText().toString());
                editor.apply();
                startActivity(new Intent(update_plant.this, view_plant.class));
            }
            }
        });


    }
}