package com.example.project_2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantBD;
import com.example.project_2.Models.userDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class search extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<plantBD> modelList;
    RecyclerView recyclerView;
    Context context=search.this;

    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    TabLayout tabLayout;
    TextView symptoms;

    String type_account;
    private DatabaseReference reference;
    private  String userID;

    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray={"Fatigue","Fever","Stomachache","Headache","Nausea","Skin irritation",
            "Indigestion","Infections and ulcers","Diarrhea","Constipation","Colds"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDB userProfile=snapshot.getValue(userDB.class);
                type_account =userProfile.getAccountType();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        selectedsymptoms = new boolean[symptomsArray.length];
        modelList =new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        databaseReference=FirebaseDatabase.getInstance().getReference().child("plants");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    modelList=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        modelList.add(ds.getValue(plantBD.class));
                    }

                    searchadapter myadapter=new searchadapter(modelList,context);
                    recyclerView.setAdapter(myadapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        symptoms=findViewById(R.id.symptoms);
        symptoms.setVisibility(View.INVISIBLE);

        searchView=findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                namesearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                namesearch(s);
                return false;
            }
        });
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        symptoms.setVisibility(View.INVISIBLE);
                        searchView.setVisibility(View.VISIBLE);


                        break;
                    case 1:
                        symptoms.setVisibility(View.VISIBLE);
                        searchView.setVisibility(View.INVISIBLE);


                        symptoms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(search.this);
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
                                }).setPositiveButton("Search", new DialogInterface.OnClickListener() {
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
                                        Symptomssearch(stringBuilder.toString());
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

                        break;

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(search.this, home.class);
                        startActivity(intent1);
                        break;
                    case R.id.ptofile:
                        if(type_account.equals("Admin Account"))
                        {
                            Intent intent2 = new Intent(search.this, AdminProfile.class);
                            startActivity(intent2);
                            break;
                        }
                        else if(type_account.equals("Herbalist Account"))
                        {
                            Intent intent2 = new Intent(search.this, HerbalistProfile.class);
                            startActivity(intent2);
                            break;
                        }
                        else
                        {
                            Intent intent2 = new Intent(search.this, user_account.class);
                            startActivity(intent2);
                            break;
                        }
                    case R.id.search:
                        startActivity(new Intent(search.this, search.class));
                        break;


                }
                return true;
            }
        });


    }



    private void namesearch(String s)
    {
        ArrayList<plantBD>list=new ArrayList<>();
        for(plantBD object: modelList){
            if(object.getName().toLowerCase().startsWith(s.toLowerCase())){
                list.add(object);
            }
        }
        searchadapter myadapter=new searchadapter(list,context);
        recyclerView.setAdapter(myadapter);


    }
    private void Symptomssearch(String s)
    {
        ArrayList<plantBD>list=new ArrayList<>();
        for(plantBD object: modelList){
            if(object.getSymptoms().toLowerCase().contains(s.toLowerCase())){
                list.add(object);
            }
        }
        searchadapter myadapter=new searchadapter(list,context);
        recyclerView.setAdapter(myadapter);




    }

}