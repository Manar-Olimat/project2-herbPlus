package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.MaterialToolbar;

public class settings extends AppCompatActivity implements View.OnClickListener {

    CardView change_image,edit_image,Privacy_image,terms_image;
    MaterialToolbar back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        change_image=findViewById(R.id.change_image);
        edit_image=findViewById(R.id.edit_image);
        Privacy_image=findViewById(R.id.Privacy_image);
        terms_image=findViewById(R.id.terms_image);
        back=findViewById(R.id.topAppBar);

        change_image.setOnClickListener(this);
        edit_image.setOnClickListener(this);
        Privacy_image.setOnClickListener(this);
        terms_image.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(settings.this, user_account.class));
                break;
            case R.id.change_image:
                startActivity(new Intent(settings.this, change_password.class));
                break;
            case R.id.edit_image:
                startActivity(new Intent(settings.this, Edit_userprofile.class));
                break;
            case R.id.Privacy_image:
                startActivity(new Intent(settings.this, privacy_policy.class));
                break;
            case R.id.terms_image:
                startActivity(new Intent(settings.this, Terms_Condition.class));
                break;
        }
    }
}