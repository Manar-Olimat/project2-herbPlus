package com.example.project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {
    Button sign_up;
    TextInputEditText gg;
    EditText email;
    EditText username;
    EditText password;
    RadioButton herbalistradio;
    RadioButton userRadio;
    CheckBox terms;

    FirebaseDatabase root;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up=findViewById(R.id.sign_up);
       gg=findViewById(R.id.TextInputEditText);
       email=findViewById(R.id.email);
       username=findViewById(R.id.username);
       password=findViewById(R.id.password);
        terms=findViewById(R.id.checkTerms);
        herbalistradio=findViewById(R.id.herbalist);
        userRadio=findViewById(R.id.userAccount);
        mAuth = FirebaseAuth.getInstance();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDataEntered();
                root=FirebaseDatabase.getInstance();
              reference=root.getReference( "users");
                // get values
                final String usernameValue=username.getText().toString();
                final String emailValue=email.getText().toString();
                final String passwordValue=password.getText().toString();
                final String accountTypeValue=userRadio.getText().toString();

                final firDB db=new firDB(usernameValue,emailValue,passwordValue,accountTypeValue);


           mAuth.createUserWithEmailAndPassword(emailValue,passwordValue)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){
                              FirebaseUser user = mAuth.getCurrentUser();

                             FirebaseDatabase.getInstance().getReference().child(user.getUid())
                                      .setValue(db);

                          }
                          else {
                            Toast.makeText(com.example.project_2.sign_up.this,"sign up faild",Toast.LENGTH_LONG).show();
                        }
                         }
                     });
                /*Intent mainIntent = new Intent(sign_up.this,user_account.class);
                sign_up.this.startActivity(mainIntent);
                sign_up.this.finish();*/
            }

            private void checkDataEntered() {
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