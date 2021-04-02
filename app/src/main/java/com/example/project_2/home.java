package com.example.project_2;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.project_2.Models.plantInfoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewTest;
    plantInfoRecyclerAdapter recyclerAdapter;
    List<plantInfoModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(home.this, home.class);
                        startActivity(intent1);
                        break;
                    case R.id.ptofile:
                        Intent intent2 = new Intent(home.this, user_account.class);
                        startActivity(intent2);
                        break;
                    case R.id.search:
                        startActivity(new Intent(home.this, search.class));
                        break;
                    case R.id.favorites:
                        startActivity(new Intent(home.this, add_plant.class));
                        break;

                }
                return true;
            }
        });

        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        Menu menu;
      //  inflater.inflate(R.menu.class, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
      //  SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
      //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
      //  searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // ******* recycler view
        recyclerViewTest=findViewById(R.id.recyclerView);

        //add data to model
        modelList=new ArrayList<>();
        modelList.add(new plantInfoModel(getString(R.string.Turmeric),R.drawable.turmeric));
        modelList.add(new plantInfoModel(getString(R.string.Catnip),R.drawable.catnip));
        modelList.add(new plantInfoModel(getString(R.string.Marigold),R.drawable.mariegold));
        modelList.add(new plantInfoModel(getString(R.string.Moonflowers),R.drawable.moonflower));
        modelList.add(new plantInfoModel(getString(R.string.Gingko),R.drawable.gingko));
        setRecyclerViewTest(modelList);
    }

    private void setRecyclerViewTest(List<plantInfoModel> modelList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTest.setLayoutManager(layoutManager);
        recyclerAdapter=new plantInfoRecyclerAdapter(this,modelList);
        recyclerViewTest.setAdapter(recyclerAdapter);


    }
}