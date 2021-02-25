package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class sign_in extends AppCompatActivity {
TextView sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sign_up=findViewById(R.id.signUp);
         sign_up.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent mainIntent = new Intent(sign_in.this,sign_up.class);
                 sign_in.this.startActivity(mainIntent);
                 sign_in.this.finish();
             }
         });
    }
}