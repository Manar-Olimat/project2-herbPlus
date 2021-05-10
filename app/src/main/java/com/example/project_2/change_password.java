package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class change_password extends AppCompatActivity implements View.OnClickListener {
    MaterialToolbar back;
    TextView Cancel;
    TextInputEditText cur_password,new_password,con_password;
    Button change_button;
    FirebaseAuth mAuth;

    private FirebaseUser user;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        reference= FirebaseDatabase.getInstance().getReference("users");
        back=findViewById(R.id.topAppBar1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(change_password.this, settings.class));
            }
        });
        Cancel=findViewById(R.id.cancel);
        Cancel.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();

        cur_password=findViewById(R.id.cur_password);
        new_password=findViewById(R.id.new_password);
        con_password=findViewById(R.id.con_password);
        change_button=findViewById(R.id.change_button);
        change_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                startActivity(new Intent(change_password.this, settings.class));
                break;
            case R.id.change_button:
                if(!cur_password.getText().toString().isEmpty()&&!new_password.getText().toString().isEmpty()
                        &&!con_password.getText().toString().isEmpty()){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), cur_password.getText().toString());

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if(new_password.getText().toString().equals(con_password.getText().toString())){

                                            user.updatePassword(new_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        reference.child(user.getUid()).child("password").setValue(new_password.getText().toString());
                                                        Toast.makeText(getApplicationContext(),"Password updated",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(change_password.this, settings.class));

                                                    } else {
                                                        Toast.makeText(getApplicationContext(),"Error password not updated",Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });}
                                        else {
                                            Toast.makeText(getApplicationContext(),"Password mismatching",Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Current Password incorrect",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter all fields.",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}