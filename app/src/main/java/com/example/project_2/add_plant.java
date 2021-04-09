package com.example.project_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_2.Models.userDB;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class add_plant extends AppCompatActivity {
    MaterialToolbar back;
    EditText plantame;
    TextView symptoms;
    MultiAutoCompleteTextView description, information;
    Button addplant;
    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray={"Fatigue","Fever","Stomachache","Headache","Nausea","Skin irritation",
            "Indigestion","Infections and ulcers","Diarrhea","Constipation","Colds"};

    FirebaseDatabase root;
    FirebaseUser user;
    FirebaseAuth mAuth ;
    DatabaseReference reference,reference_user;
    String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        plantame =findViewById(R.id.plantname);
        symptoms = findViewById(R.id.symptoms);
        description=findViewById(R.id.description);
        information=findViewById(R.id.information);
        addplant=findViewById(R.id.addplant);
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        selectedsymptoms = new boolean[symptomsArray.length];
        back=findViewById(R.id.topAppBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_plant.this, home.class));

            }
        });

        addplant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
                if (!plantame.getText().toString().isEmpty() && !symptoms.getText().toString().isEmpty() &&
                        !description.getText().toString().isEmpty() &&!information.getText().toString().isEmpty()  ){
                    final String plantnameValue = plantame.getText().toString();
                    final String symptomsValue = symptoms.getText().toString();
                    final String descriptionValue = description.getText().toString();
                    final String informationValue = information.getText().toString();

                    userID=user.getUid();
                    reference_user= FirebaseDatabase.getInstance().getReference("users");
                    reference_user.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userDB userProfile=snapshot.getValue(userDB.class);
                            if(userProfile.getAccountType().equals("Admin Account")){
                                reference = root.getReference("plants");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(plantnameValue)) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(add_plant.this);
                                            builder1.setMessage("The plant already exists. Do you want to modify the data?");
                                            builder1.setCancelable(false);

                                            builder1.setPositiveButton(
                                                    "Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            reference = root.getReference("plants").child(plantnameValue);
                                                            reference.child("name").setValue(plantnameValue);
                                                            reference.child("symptoms").setValue(symptomsValue);
                                                            reference.child("description").setValue(descriptionValue);
                                                            reference.child("information").setValue(informationValue);


                                                            Intent mainIntent = new Intent(add_plant.this, home.class);
                                                            add_plant.this.startActivity(mainIntent);
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
                                        } else {

                                            reference = root.getReference("plants").child(plantnameValue);
                                            reference.child("name").setValue(plantnameValue);
                                            reference.child("symptoms").setValue(symptomsValue);
                                            reference.child("description").setValue(descriptionValue);
                                            reference.child("information").setValue(informationValue);


                                            Intent mainIntent = new Intent(add_plant.this, home.class);
                                            add_plant.this.startActivity(mainIntent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(add_plant.this);
                                builder1.setMessage("The plant has been add but it need for the admin confirmation...");
                                builder1.setCancelable(false);
                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            public void onClick(DialogInterface dialog, int id) {
                                                Date date = new Date();
                                                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                                reference = root.getReference("confirm_plants").child(plantnameValue);
                                                reference.child("name").setValue(plantnameValue);
                                                reference.child("symptoms").setValue(symptomsValue);
                                                reference.child("description").setValue(descriptionValue);
                                                reference.child("information").setValue(informationValue);
                                                reference.child("added_by").setValue(userProfile.getUsername());
                                                reference.child("date").setValue(localDate.getMonthValue()+"/"+ localDate.getDayOfMonth()+"/"+ localDate.getYear());

                                                Intent mainIntent = new Intent(add_plant.this, home.class);
                                                add_plant.this.startActivity(mainIntent);
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
                                alert11.show();                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_plant.this);
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


    }
    private void checkDataEntered() {
        if (isEmpty(plantame)) {
            plantame.setError("Empty plant name");
        }
        if ( isEmpty(symptoms)) {
            symptoms.setError("Empty symptoms");

        }
        if (isEmpty(description)) {
            description.setError("Empty description");
        }
        if (isEmpty(information)) {
            information.setError("Empty information");
        }


    }

    boolean isEmpty( EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmpty( TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }boolean isEmpty( MultiAutoCompleteTextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

}