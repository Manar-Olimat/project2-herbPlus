package com.example.project_2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_2.Models.userDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class Edit_userprofile extends AppCompatActivity implements View.OnClickListener {
    MaterialToolbar back;
    FloatingActionButton editUserPhoto;
    Button updateProfile;
    TextInputEditText username;
    RadioButton herbalistradio;
    RadioButton userRadio;
    TextInputEditText email;
    ImageView profile_image;
    String password=null;

    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;
    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private StorageReference mStorageRef;
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
        profile_image=findViewById(R.id.profile_image2);
        back=findViewById(R.id.topAppBar);
        back.setOnClickListener(this);


        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        mStorageRef = FirebaseStorage.getInstance().getReference("users");

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
                Glide.with(Edit_userprofile.this).load(userProfile.getProfile_image()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.user)).into(profile_image);
                username.setText(usernameValue);
                password=userProfile.getPassword();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                String finalAccountTypeValue = accountTypeValue;
                reference.child(userID).child("username").setValue(usernameValue);
                reference.child(userID).child("accountType").setValue(finalAccountTypeValue);
                reference.child(userID).child("email").setValue(emailValue);
                reference.child(userID).child("id").setValue(user.getUid());

                AuthCredential credential = EmailAuthProvider
                        .getCredential(Objects.requireNonNull(user.getEmail()), password);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updateEmail(emailValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                reference.child(user.getUid()).child("email").setValue(emailValue);

                                            }
                                        }
                                    });
                                }
                            }
                        });

                if (mImageUri != null) {
                    mStorageRef = mStorageRef.child(user.getUid()
                            + "." + getFileExtension(mImageUri));
                    mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUrl = uri;

                                    reference.child(userID).child("profile_image").setValue(downloadUrl.toString());

                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Edit_userprofile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                }
                            });
                }
                            startActivity(new Intent(Edit_userprofile.this, user_account.class));

                break;
            case R.id.editProfilePhoto1:
                selectImage();

                break;

        }

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Glide.with(Edit_userprofile.this).load(mImageUri).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.user)).into(profile_image);

        }
        else  if (requestCode == REQUEST_TAKE_PHOTO) {
            Glide.with(Edit_userprofile.this).load(mImageUri).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.user)).into(profile_image);


        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Edit_userprofile.this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                TakePicture();
            } else if (items[item].equals("Choose from Library")) {
                openFileChooser();
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TakePicture(){
        if (ContextCompat.checkSelfPermission(Edit_userprofile.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Edit_userprofile.this, new String[]{
                    Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From te Camera");
        mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraInter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraInter.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(cameraInter, REQUEST_TAKE_PHOTO);
    }

}