package com.example.project_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_account extends AppCompatActivity implements View.OnClickListener {
    CardView Notification;
    CardView logout;
    FloatingActionButton edit;
    TextView username;
    ShapeableImageView userPhoto;
    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        Notification = findViewById(R.id.Notification);

        username=findViewById(R.id.username_viewProfile1);
        userPhoto=findViewById(R.id.profile_image2);
        edit=findViewById(R.id.edit_userProfile);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        edit.setOnClickListener(this);
        logout=findViewById(R.id.LogOut);
        logout.setOnClickListener(this);

        user= FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userDB userProfile=snapshot.getValue(userDB.class);
                username.setText(userProfile.getUsername());
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

        }
    }
}