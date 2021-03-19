package com.example.project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity implements View.OnClickListener {
    Button sign_up;
    TextInputEditText username;
    TextInputEditText email;
    TextInputEditText password;
    RadioButton herbalistradio;
    RadioButton userRadio;
    CheckBox terms;
    TextView back_signin;
    FirebaseDatabase root;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up=findViewById(R.id.updateProfile);
        username=findViewById(R.id.username);
       email=findViewById(R.id.email);
       password=findViewById(R.id.password);
        terms=findViewById(R.id.checkTerms);
        herbalistradio=findViewById(R.id.herbalist);
        userRadio=findViewById(R.id.userAccount);
        mAuth = FirebaseAuth.getInstance();

        sign_up.setOnClickListener(this);
        back_signin=findViewById(R.id.back11);
        back_signin.setOnClickListener(this);





        }


    private void checkDataEntered() {
        if (isEmpty(username)) {
            username.setError("Empty username");
            username.requestFocus();
            return;
        }
        if (isEmpty(password)) {
            password.setError("Empty password");
            password.requestFocus();
            return;
        }
        if (!isEmail(email)){
            email.setError("Enter valid email!");
email.requestFocus();
            return;}
        if(!herbalistradio.isChecked() && !userRadio.isChecked())
            herbalistradio.setError("choose your account type");

    }
    boolean isEmail(TextInputEditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    boolean isEmpty( TextInputEditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back11:
                startActivity(new Intent(sign_up.this, sign_in.class));
break;
            case R.id.updateProfile:
                checkDataEntered();
                root=FirebaseDatabase.getInstance();
                reference=FirebaseDatabase.getInstance().getReference("users");
                // get values
                final String usernameValue=username.getText().toString();
                final String emailValue=email.getText().toString();
                final String passwordValue=password.getText().toString();
                String accountTypeValue=userRadio.getText().toString();
if(userRadio.isChecked())
    accountTypeValue=userRadio.getText().toString();
else if(herbalistradio.isChecked())
    accountTypeValue=herbalistradio.getText().toString();
                final userDB db=new userDB(usernameValue,emailValue,passwordValue,accountTypeValue);

if (!emailValue.isEmpty()&& !passwordValue.isEmpty())
            {  mAuth.createUserWithEmailAndPassword(emailValue,passwordValue)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                     user = mAuth.getCurrentUser();

                                    FirebaseDatabase.getInstance().getReference().child(user.getUid())
                                            .setValue(db);

                                }
                                else {
                                    Toast.makeText(com.example.project_2.sign_up.this,"sign up faild",Toast.LENGTH_LONG).show();
                                }
                            }
                        });}
                /*checkDataEntered();
                root=FirebaseDatabase.getInstance();
                reference=root.getReference( "users");
                // get values
                final String usernameValue=username.getText().toString();
                final String emailValue=email.getText().toString();
                final String passwordValue=password.getText().toString();
                String accountTypeValue=userRadio.getText().toString();

                if(userRadio.isChecked())
                    accountTypeValue=userRadio.getText().toString();
                else if (herbalistradio.isChecked())
                    accountTypeValue=herbalistradio.getText().toString();
                final userDB db=new userDB(usernameValue,emailValue,passwordValue,accountTypeValue);


                mAuth.createUserWithEmailAndPassword(emailValue,passwordValue)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    FirebaseDatabase.getInstance().getReference().child(user.getUid())
                                            .setValue(db);
                                    Intent mainIntent = new Intent(sign_up.this,user_account.class);
                                    sign_up.this.startActivity(mainIntent);
                                    sign_up.this.finish();
                                }
                                else {
                                    Toast.makeText(com.example.project_2.sign_up.this,"sign up faild",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                break;*/
        }
    }
}
