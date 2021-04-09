package com.example.project_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewConfirm extends AppCompatActivity {
    TextView name,symptom,description,info,date;
    Button Accept,Reject;
    FirebaseDatabase root;
    FirebaseAuth mAuth ;
    DatabaseReference reference ,ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_confirm);
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        name=findViewById(R.id.name);
        symptom=findViewById(R.id.symptom);
        description=findViewById(R.id.description);
        info=findViewById(R.id.info);
        date=findViewById(R.id.date);
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        String name1 = prefs.getString("name", "No name defined");
        String Symptoms = prefs.getString("Symptoms", "No name defined");
        String description1 = prefs.getString("description", "No name defined");
        String information = prefs.getString("information", "No name defined");
        String dat = prefs.getString("date", "No name defined");

        name.setText(name1);
        symptom.setText(Symptoms);
        description.setText(description1);
        info.setText(information);
        date.setText(dat);
        Accept=findViewById(R.id.Accept);
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = root.getReference("plants");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(name1)) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewConfirm.this);
                        builder1.setMessage("The plant already exists. Do you want to modify the data?");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ref = FirebaseDatabase.getInstance().getReference("confirm_plants").child(name1);
                                        ref.removeValue();

                                        reference = root.getReference("plants").child(name1);
                                        reference.child("name").setValue(name1);
                                        reference.child("symptoms").setValue(Symptoms);
                                        reference.child("description").setValue(description1);
                                        reference.child("information").setValue(information);
                                        Intent mainIntent = new Intent(ViewConfirm.this, confirm_list.class);
                                        ViewConfirm.this.startActivity(mainIntent);
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else{
                        ref = FirebaseDatabase.getInstance().getReference("confirm_plants").child(name1);
                        ref.removeValue();
                        reference = root.getReference("plants").child(name1);
                        reference.child("name").setValue(name1);
                        reference.child("symptoms").setValue(Symptoms);
                        reference.child("description").setValue(description1);
                        reference.child("information").setValue(information);
                        Intent mainIntent = new Intent(ViewConfirm.this, confirm_list.class);
                        ViewConfirm.this.startActivity(mainIntent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            }
        });
        Reject=findViewById(R.id.Reject);
        Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference("confirm_plants").child(name1);
                ref.removeValue();
                Intent mainIntent = new Intent(ViewConfirm.this, confirm_list.class);
                ViewConfirm.this.startActivity(mainIntent);

            }
        });
    }
}