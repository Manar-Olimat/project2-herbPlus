package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    ImageView change_image,edit_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        change_image=findViewById(R.id.change_image);
        edit_image=findViewById(R.id.edit_image);
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, change_password.class));
            }
        });
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, Edit_userprofile.class));
            }
        });
    }
}