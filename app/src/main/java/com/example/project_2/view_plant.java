package com.example.project_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class view_plant extends AppCompatActivity {

    TextView name,symptom,description,information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant);
        name=findViewById(R.id.name);
        symptom=findViewById(R.id.symptom);
        description=findViewById(R.id.description);
        information=findViewById(R.id.information);
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        String name1 = prefs.getString("name", "No name defined");
        String Symptoms = prefs.getString("Symptoms", "No name defined");
        String description1 = prefs.getString("description", "No name defined");
        String information1 = prefs.getString("information", "No name defined");

        //Bundle bundle = getIntent().getExtras();
        name.setText(name1);
        symptom.setText(Symptoms);
        description.setText(description1);
        information.setText(information1);

    }
}