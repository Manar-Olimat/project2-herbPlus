package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class sign_up extends AppCompatActivity {
    Button sign_up;
    TextInputEditText gg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up=findViewById(R.id.sign_up);
       gg=findViewById(R.id.TextInputEditText);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(sign_up.this,home.class);
                sign_up.this.startActivity(mainIntent);
                sign_up.this.finish();
            }
        });
    }
}