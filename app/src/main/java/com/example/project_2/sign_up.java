package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class sign_up extends AppCompatActivity {
    Button sign_up;
    TextInputEditText username;
    EditText email;
    EditText password;
    RadioButton herbalistradio;
    RadioButton userRadio;
    CheckBox terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up=findViewById(R.id.sign_up);
        username=findViewById(R.id.username);
       email=findViewById(R.id.email);
       password=findViewById(R.id.password);
        terms=findViewById(R.id.checkTerms);
        herbalistradio=findViewById(R.id.herbalist);
        userRadio=findViewById(R.id.userAccount);


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDataEntered();
               Intent mainIntent = new Intent(sign_up.this,user_account.class);
                sign_up.this.startActivity(mainIntent);
                sign_up.this.finish();
            }

            private void checkDataEntered() {
                if (isEmpty(username)) {
                    username.setError("Empty username");
                }
                if (isEmpty(password)) {
                   password.setError("Empty password");
                }
                if (!isEmail(email))
                    email.setError("Enter valid email!");

                if(!herbalistradio.isChecked() && !userRadio.isChecked())
                    herbalistradio.setError("choose your account type");

            }
            boolean isEmail(EditText text) {
                CharSequence email = text.getText().toString();
                return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
            }
            boolean isEmpty(EditText text) {
                CharSequence str = text.getText().toString();
                return TextUtils.isEmpty(str);
            }

        });
    }
}