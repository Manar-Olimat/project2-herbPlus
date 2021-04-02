package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class view_plant extends AppCompatActivity {

    TextView name,symptom,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant);
        name=findViewById(R.id.name);
        symptom=findViewById(R.id.symptom);
        description=findViewById(R.id.description);
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        String name1 = prefs.getString("name", "No name defined");
        String Symptoms = prefs.getString("Symptoms", "No name defined");
        String description1 = prefs.getString("description", "No name defined");

        //Bundle bundle = getIntent().getExtras();
        name.setText(name1);
        symptom.setText(Symptoms);
        description.setText(description1);


    }
}