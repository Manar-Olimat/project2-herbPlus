package com.example.project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
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


    }
}