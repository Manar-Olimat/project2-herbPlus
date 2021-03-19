package com.example.project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_in extends AppCompatActivity {
    TextView sign_up;
    TextInputEditText username;
    TextInputEditText password;
    Button signIn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sign_up=findViewById(R.id.signUp);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password_signIn);
        signIn=findViewById(R.id.sign_in);
        mAuth=FirebaseAuth.getInstance();
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sign_in.this, sign_up.class));

            }
        });
signIn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();

        if (usernameValue.isEmpty()) {
            username.setError(" email required!");
            username.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
            username.setError("Enter valid email!");
            username.requestFocus();
            return;
        }
        if (passwordValue.isEmpty()) {
            password.setError("password required");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(usernameValue, passwordValue)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            startActivity(new Intent(sign_in.this, home.class));
                        } else {
                            Toast.makeText(com.example.project_2.sign_in.this, "sign in faild", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
});

       }
  /*  public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:
                Intent mainIntent = new Intent(sign_in.this,sign_up.class);
                sign_in.this.startActivity(mainIntent);
                sign_in.this.finish();
                break;
            case R.id.sign_in:
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();

                if (usernameValue.isEmpty()) {
                    username.setError(" email required!");
                    username.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
                    username.setError("Enter valid email!");
                    username.requestFocus();
                    return;
                }
                if (passwordValue.isEmpty()) {
                    password.setError("password required");
                    password.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(usernameValue, passwordValue)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    startActivity(new Intent(sign_in.this, home.class));
                                } else {
                                    Toast.makeText(com.example.project_2.sign_in.this, "sign in faild", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                break;
        }
        }*/
    }
