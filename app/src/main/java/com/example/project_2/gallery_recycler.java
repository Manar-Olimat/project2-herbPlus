package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantTypeGalleryModel;

import java.util.ArrayList;
import java.util.List;

public class gallery_recycler extends AppCompatActivity {
    RecyclerView galleryRecyclerView;
    plantTypeGalleryAdapter galleryAdapter;
    List<plantTypeGalleryModel> galleryModels;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_recycler);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(home.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        textView = findViewById(R.id.intentMsg);
        textView.setText(message);
        galleryRecyclerView=findViewById(R.id.rec22);
        galleryModels=new ArrayList<>();
        imageView=findViewById(R.id.coverImg);

        if ("Fruits".equals(textView.getText())) {
            //change cover photo
            imageView.setImageResource(R.drawable.fruit);
            //add data to model
            galleryModels.add(new plantTypeGalleryModel("Banana","Orange",R.drawable.bananas,R.drawable.oranges));
            galleryModels.add(new plantTypeGalleryModel("Berries","Cranberries",R.drawable.berries,R.drawable.cranberries));
            galleryModels.add(new plantTypeGalleryModel("Kiwis","Pears",R.drawable.kiwis,R.drawable.pears));

            setRecyclerViewTest(galleryModels);
        }
        else if ("Vegetables".equals(textView.getText())) {
//change cover photo
            imageView.setImageResource(R.drawable.vegetables);

            galleryModels.add(new plantTypeGalleryModel("Onions","Potato",R.drawable.onion,R.drawable.potato));
            galleryModels.add(new plantTypeGalleryModel("Carrot","Spinach",R.drawable.carrot,R.drawable.spinach));


            setRecyclerViewTest(galleryModels);
        }
        else if ("Leaf".equals(textView.getText())) {
//change cover photo
            imageView.setImageResource(R.drawable.leaf);

            galleryModels.add(new plantTypeGalleryModel("Kale","Microgreens",R.drawable.kale,R.drawable.microgreens));
            galleryModels.add(new plantTypeGalleryModel("Cabbage","Watercress",R.drawable.cabbage,R.drawable.watercress));


            setRecyclerViewTest(galleryModels);
        }
        else if ("Flower".equals(textView.getText())) {
//change cover photo
            imageView.setImageResource(R.drawable.flower);

            galleryModels.add(new plantTypeGalleryModel("Sunflower","Marigold",
                    R.drawable.sunflower,R.drawable.mariegold));
            galleryModels.add(new plantTypeGalleryModel("Birds of Paradise","Asiatic Lily",
                    R.drawable.birds_of_paradise,R.drawable.asiatic_lily));


            setRecyclerViewTest(galleryModels);
        }
        else //if ("Trees".equals(textView))
        {
            //change cover photo
            imageView.setImageResource(R.drawable.tree);

            galleryModels.add(new plantTypeGalleryModel("oak tree\n" ,"japanese maple",
                    R.drawable.oak_tree,R.drawable.japanese_maple));


            setRecyclerViewTest(galleryModels);
        }

    }

    private void setRecyclerViewTest(List<plantTypeGalleryModel> galleryModels) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        galleryRecyclerView.setLayoutManager(layoutManager);
        galleryAdapter=new plantTypeGalleryAdapter(this,galleryModels );
        galleryRecyclerView.setAdapter(galleryAdapter);
    }}