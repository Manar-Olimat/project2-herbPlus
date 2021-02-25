package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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