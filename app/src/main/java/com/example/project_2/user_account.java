package com.example.project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
   RelativeLayout Notification;
    RelativeLayout logout;
    FloatingActionButton edit;
    TextView username;
   ShapeableImageView userPhoto;
   private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
       Notification = findViewById(R.id.Notification);
     username=findViewById(R.id.username_viewProfile1);
      userPhoto=findViewById(R.id.profile_image2);
      edit=findViewById(R.id.edit_userProfile);
      edit.setOnClickListener(this);
      logout=findViewById(R.id.LogOut);
      logout.setOnClickListener(this);

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userID=user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDB userProfile=snapshot.getValue(userDB.class);

                if (userProfile!=null){

                    final String usernameValue=userProfile.username;


                    username.setText(usernameValue);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(com.example.project_2.user_account.this, " Something wrong happened!",Toast.LENGTH_LONG).show();

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
        }
    }
}