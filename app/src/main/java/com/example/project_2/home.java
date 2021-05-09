package com.example.project_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantGalleryModel;
import com.example.project_2.Models.plantInfoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewTest;
    plantInfoRecyclerAdapter recyclerAdapter;
    List<plantInfoModel> modelList;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static int itemPosition=0;
    RecyclerView galleryRecyclerView;
    plantGalleryAdapter galleryAdapter;
    List<plantGalleryModel> galleryModels;

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
                        Intent intent1 = new Intent(home.this, add_plant.class);
                        startActivity(intent1);
                        break;
                    case R.id.ptofile:

                        Intent intent2 = new Intent(home.this, user_account.class);
                        startActivity(intent2);
                        break;
                    case R.id.search:
                        startActivity(new Intent(home.this, search.class));
                        break;

                }
                return true;
            }
        });


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
        
        //*********************************
        galleryRecyclerView=findViewById(R.id.recTest);

        //add data to model
        galleryModels=new ArrayList<>();
        galleryModels.add(new plantGalleryModel("Fruit",R.drawable.fruit,R.drawable.fruits_icon1));
        galleryModels.add(new plantGalleryModel("Leaf",R.drawable.leaf,R.drawable.leaf_icon));
        galleryModels.add(new plantGalleryModel("Flower",R.drawable.flower,R.drawable.flower_icon1));
        galleryModels.add(new plantGalleryModel("Trees",R.drawable.tree,R.drawable.tree_icon1));
        galleryModels.add(new plantGalleryModel("Seeds",R.drawable.seeds,R.drawable.seeds_icon));
        galleryModels.add(new plantGalleryModel("Roots",R.drawable.root,R.drawable.root_icon));

        setRecyclerViewGallery(galleryModels);
    }

    private void setRecyclerViewGallery(List<plantGalleryModel> galleryModels1) {

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        galleryRecyclerView.setLayoutManager(layoutManager);
        galleryAdapter=new plantGalleryAdapter(this,galleryModels1 , this::onGalleryClick);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

    /*******************************************************
     *
to make the recycler view clickable
*************************************************************     */
    public void onGalleryClick(int position) {
        itemPosition=position;
        //    Log.d("TAG","onGalleryClick : clicked.");
        //   if (galleryModels.get(position).name)
        // Intent intent=new Intent(this,galleryRecycler.class);
        // startActivity(intent);
        Intent intent = new Intent(this, gallery_recycler.class);
        String message = galleryModels.get(itemPosition).getName();
        // String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("gallery", MODE_PRIVATE).edit();
        editor.putString("message",message);

        editor.apply();
        startActivity(intent);
    }

    private void setRecyclerViewTest(List<plantInfoModel> modelList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTest.setLayoutManager(layoutManager);
        recyclerAdapter=new plantInfoRecyclerAdapter(this,modelList);
        recyclerViewTest.setAdapter(recyclerAdapter);
        final int speedScroll = 2000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;
            @Override
            public void run() {
                if(count < recyclerAdapter.getItemCount()){
                    if(count==recyclerAdapter.getItemCount()-1){
                        flag = false;
                    }else if(count == 0){
                        flag = true;
                    }
                    if(flag)
                        count++;
                    else
                        count =0;

                    recyclerViewTest.smoothScrollToPosition(count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };

        handler.postDelayed(runnable,speedScroll);

    }

}