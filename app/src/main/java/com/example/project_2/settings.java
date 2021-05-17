package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project_2.Models.userDB;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settings extends AppCompatActivity implements View.OnClickListener {

    CardView change_image,edit_image,Privacy_image,terms_image;
    MaterialToolbar back;
    String type_account;
    private DatabaseReference reference;
    private  String userID;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topAppBar:
                if(type_account.equals("Admin Account"))
                {
                    Intent intent2 = new Intent(settings.this, AdminProfile.class);
                    startActivity(intent2);
                    break;
                }
                else if(type_account.equals("Herbalist Account"))
                {
                    Intent intent2 = new Intent(settings.this, HerbalistProfile.class);
                    startActivity(intent2);
                    break;
                }
                else
                {
                    Intent intent2 = new Intent(settings.this, user_account.class);
                    startActivity(intent2);
                    break;
                }
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