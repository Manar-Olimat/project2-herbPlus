package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class user_account extends AppCompatActivity {
 /*   RelativeLayout Notification;
    FloatingActionButton edit;
    TextView username;
   // ShapeableImageView userPhoto;
   private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
     //0   Notification = findViewById(R.id.Notification);
        //username=findViewById(R.id.username_viewProfile);
       // userPhoto=findViewById(R.id.profile_image);
    }
   /* public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                startActivity(new Intent(user_account.this, Edit_userprofile.class));
                break;

        }

        }*/
}