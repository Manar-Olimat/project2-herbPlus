package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_2.Models.userDB;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_userprofile extends AppCompatActivity implements View.OnClickListener {
    MaterialToolbar back;
    FloatingActionButton editUserPhoto;
    Button updateProfile;
    TextInputEditText username;
    RadioButton herbalistradio;
    RadioButton userRadio;
    TextInputEditText email;
    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userprefile);
        username=findViewById(R.id.newUsername1);
        email=findViewById(R.id.email_editProfile);

        herbalistradio=findViewById(R.id.herbalist_edit);
        userRadio=findViewById(R.id.userAccount_edit);
        updateProfile=findViewById(R.id.updateProfile2);
        updateProfile.setOnClickListener(this);
        editUserPhoto=findViewById(R.id.editProfilePhoto1);
        editUserPhoto.setOnClickListener(this);
        back=findViewById(R.id.topAppBar);
        back.setOnClickListener(this);

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userID=user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            userDB userProfile=snapshot.getValue(userDB.class);

            if (userProfile !=null){

                final String usernameValue= userProfile.getUsername();
                final String emailValue= userProfile.getEmail();
                //final String passwordValue=password.getText().toString();
                String accountTypeValue= userProfile.getAccountType();

                username.setText(usernameValue);
                email.setText(emailValue);
                if (accountTypeValue.equals("Herbalist Account"))
                    herbalistradio.setChecked(true);
                else if (accountTypeValue.equals("User Account"))
                        userRadio.setChecked(true);


            }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(com.example.project_2.Edit_userprofile.this, " Something wrong happened!",Toast.LENGTH_LONG).show();

    }
});
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.topAppBar:
                startActivity(new Intent(Edit_userprofile.this, settings.class));
               break;
            case R.id.updateProfile2:
                final String usernameValue=username.getText().toString().trim();
                final String emailValue=email.getText().toString().trim();
                 String accountTypeValue=herbalistradio.getText().toString().trim();
                if(herbalistradio.isChecked())
                    accountTypeValue=herbalistradio.getText().toString().trim();
                else if( userRadio.isChecked())
                    accountTypeValue=userRadio.getText().toString().trim();
                if (usernameValue.isEmpty()) {
                    username.setError(" username required!");
                    username.requestFocus();
                    return;
                }
                if (accountTypeValue.isEmpty()) {
                    herbalistradio.setError("choose your account type");
                    herbalistradio.requestFocus();
                    return;
                }
                if (emailValue.isEmpty()) {
                    email.setError(" email required!");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                    email.setError("Enter valid email!");
                    email.requestFocus();
                    return;
                }

                reference.child(userID).child("username").setValue(usernameValue);
                reference.child(userID).child("email").setValue(emailValue);
                reference.child(userID).child("accountType").setValue(accountTypeValue);

                break;
            case R.id.editProfilePhoto1:

                break;

        }

    }
}