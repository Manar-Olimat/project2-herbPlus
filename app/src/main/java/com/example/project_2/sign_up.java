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
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class sign_up extends AppCompatActivity implements View.OnClickListener {
    Button sign_up;
    TextInputEditText username;
    TextInputEditText email;
    TextInputEditText password;
    RadioButton herbalistradio;
    RadioButton userRadio;
    CheckBox terms;
    TextView terms_con, back11;
    FirebaseDatabase root;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    ImageView showprofile_image;
    FloatingActionButton profile_img;
    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri=null;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up = findViewById(R.id.updateProfile);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        terms = findViewById(R.id.checkTerms);
        herbalistradio = findViewById(R.id.herbalist);
        userRadio = findViewById(R.id.userAccount);
        mAuth = FirebaseAuth.getInstance();
        sign_up.setOnClickListener(this);
        back11 = findViewById(R.id.back11);
        back11.setOnClickListener(this);
        terms_con = findViewById(R.id.terms_con);
        terms_con.setOnClickListener(this);
        showprofile_image = findViewById(R.id.showprofile_image);
        profile_img = findViewById(R.id.profile_img);
        profile_img.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("users");


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

        if (!herbalistradio.isChecked() && !userRadio.isChecked())
            herbalistradio.setError("choose your account type");

    }

    boolean isEmail(TextInputEditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(TextInputEditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateProfile:
                checkDataEntered();
                if (!username.getText().toString().isEmpty() && !email.getText().toString().isEmpty() &&
                        !password.getText().toString().isEmpty()&& (userRadio.isChecked() || herbalistradio.isChecked()) && terms.isChecked()) {
                    root = FirebaseDatabase.getInstance();
                    final String usernameValue = username.getText().toString();
                    final String emailValue = email.getText().toString();
                    final String passwordValue = password.getText().toString();
                    String accountTypeValue = userRadio.getText().toString();
                    userRadio.setError(null);
                    herbalistradio.setError(null);

                    if (userRadio.isChecked())
                        accountTypeValue = userRadio.getText().toString();
                    else if (herbalistradio.isChecked())
                        accountTypeValue = herbalistradio.getText().toString();


                    final String finalAccountTypeValue = accountTypeValue;
                    mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        reference = root.getReference("users").child(user.getUid());
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

                                                            if(downloadUrl!=null){
                                                                reference.child("username").setValue(usernameValue);
                                                                reference.child("email").setValue(emailValue);
                                                                reference.child("password").setValue(passwordValue);
                                                                reference.child("accountType").setValue(finalAccountTypeValue);
                                                                reference.child("profile_image").setValue(downloadUrl.toString());
                                                                Intent mainIntent = new Intent(sign_up.this, home.class);
                                                                sign_up.this.startActivity(mainIntent);
                                                                sign_up.this.finish();
                                                            }else {
                                                                Toast.makeText(sign_up.this, "No file selected", Toast.LENGTH_SHORT).show();

                                                            }

                                                        }
                                                    });

                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(sign_up.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                        }
                                                    });
                                        }

                                    }
                                    else {
                                        Toast.makeText(com.example.project_2.sign_up.this, "sign up failed", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }
                break;
            case R.id.back11:
                startActivity(new Intent(sign_up.this, sign_in.class));
                break;
            case R.id.terms_con:
                Intent intent = new Intent(sign_up.this, Terms_Condition.class);
                intent.putExtra("sign_up", true);
                startActivity(intent);
                break;
            case R.id.profile_img:
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
            Glide.with(sign_up.this).load(mImageUri).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.user)).into(showprofile_image);

        }
        else  if (requestCode == REQUEST_TAKE_PHOTO) {
            Glide.with(sign_up.this).load(mImageUri).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.user)).into(showprofile_image);


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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(sign_up.this);
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
       if (ContextCompat.checkSelfPermission(sign_up.this, Manifest.permission.CAMERA) !=
               PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(sign_up.this, new String[]{
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
