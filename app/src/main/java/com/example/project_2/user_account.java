package com.example.project_2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project_2.Models.userDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_account extends AppCompatActivity implements View.OnClickListener {
    CardView Notification;
    CardView logout, help,setting;
    FloatingActionButton edit;
    TextView username;
    ShapeableImageView userPhoto;
    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;
    String type_account;
    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        Notification = findViewById(R.id.Notification);

        username=findViewById(R.id.username_viewProfile1);
        userPhoto=findViewById(R.id.profile_image2);
        edit=findViewById(R.id.edit_userProfile);
        help=findViewById(R.id.help);
        setting=findViewById(R.id.settings);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setting.setOnClickListener(this);
        edit.setOnClickListener(this);
        logout=findViewById(R.id.LogOut);
        logout.setOnClickListener(this);
        help.setOnClickListener(this);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userDB userProfile=snapshot.getValue(userDB.class);
                type_account =userProfile.getAccountType();
                username.setText(userProfile.getUsername()+" "+user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(user_account.this, home.class);
                        startActivity(intent1);
                        break;
                    case R.id.ptofile:
                        Intent intent2 = new Intent(user_account.this, user_account.class);
                        startActivity(intent2);
                        break;
                    case R.id.search:
                        startActivity(new Intent(user_account.this, search.class));
                        break;


                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_userProfile :
                startActivity(new Intent(user_account.this, Edit_userprofile.class));

                break;
            case R.id.LogOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(user_account.this, sign_in.class));

                break;
            case R.id.help:
                if(type_account.equals("Admin Account")) {
                    startActivity(new Intent(user_account.this, confirm_list.class));
                }
                break;
            case R.id.settings:
                startActivity(new Intent(user_account.this, settings.class));

                break;


        }
    }
}