package com.example.project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_2.Models.plantBD;
import com.example.project_2.Models.plantInfoModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class search extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<plantBD> modelList;
    RecyclerView recyclerView;
    Context context=search.this;

    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    TabLayout tabLayout;
    TextView symptoms;

    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray={"Fatigue","Fever","Stomachache","Headache","Nausea","Skin irritation",
            "Indigestion","Infections and ulcers","Diarrhea","Constipation","Colds"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        selectedsymptoms = new boolean[symptomsArray.length];
        modelList =new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference=FirebaseDatabase.getInstance().getReference().child("plants");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    modelList=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        modelList.add(ds.getValue(plantBD.class));
                    }

                    Myadapter myadapter=new Myadapter(modelList,context);
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
                        Intent intent2 = new Intent(search.this, user_account.class);
                        startActivity(intent2);
                        break;
                    case R.id.search:
                        startActivity(new Intent(search.this, search.class));
                        break;
                    case R.id.favorites:
                        startActivity(new Intent(search.this, add_plant.class));
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
        Myadapter myadapter=new Myadapter(list,context);
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
        Myadapter myadapter=new Myadapter(list,context);
        recyclerView.setAdapter(myadapter);




    }

}