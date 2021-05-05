package com.example.project_2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;

public class update_plant extends AppCompatActivity implements View.OnClickListener {
    MaterialToolbar back;
    TextView plantname, symptoms;
    MultiAutoCompleteTextView description, information;
    Button updateplant;
    FloatingActionButton edit_plant;
    ImageView plant_image;
    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray={"Fatigue","Fever","Stomachache","Headache","Nausea","Skin irritation",
            "Indigestion","Infections and ulcers","Diarrhea","Constipation","Colds"};


    DatabaseReference reference;
    String name1 ,Symptoms ,description1 ,information1 ,image_plant1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plant);
        plantname =findViewById(R.id.plantname);
        symptoms = findViewById(R.id.symptoms);
        description=findViewById(R.id.description);
        information=findViewById(R.id.information);
        updateplant=findViewById(R.id.updateplant);
        edit_plant=findViewById(R.id.edit_plant);
        plant_image=findViewById(R.id.plant_image);
        mStorageRef = FirebaseStorage.getInstance().getReference("users");

        selectedsymptoms = new boolean[symptomsArray.length];
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        name1 = prefs.getString("name", "No name defined");
        Symptoms = prefs.getString("Symptoms", "No Symptoms defined");
        description1 = prefs.getString("description", "No description defined");
        information1 = prefs.getString("information", "No information defined");
        image_plant1=prefs.getString("plant_image", "No name defined");

        plantname.setText(name1);
        symptoms.setText(Symptoms);
        description.setText(description1);
        information.setText(information1);
        Glide.with(update_plant.this).load(image_plant1).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_image);

        back=findViewById(R.id.topAppBar);
        back.setOnClickListener(this);
        symptoms.setOnClickListener(this);
        updateplant.setOnClickListener(this);
        edit_plant.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(update_plant.this, view_plant.class));
                break;
            case R.id.edit_plant:
                selectImage();
                break;
            case R.id.symptoms:
                AlertDialog.Builder builder = new AlertDialog.Builder(update_plant.this);
                builder.setTitle("Selected Symptoms...");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(symptomsArray, selectedsymptoms, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            symptomsList.add(which);
                            Collections.sort(symptomsList);
                        } else if (symptomsList.contains(which)) {
                            symptomsList.remove(Integer.valueOf(which));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < symptomsList.size(); i++) {
                            stringBuilder.append(symptomsArray[symptomsList.get(i)]);
                            if (i != symptomsList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        symptoms.setText(stringBuilder.toString());
                        symptoms.setError(null);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedsymptoms.length; i++) {
                            selectedsymptoms[i] = false;
                            symptomsList.clear();
                            symptoms.setText("");
                        }
                    }
                }).show();

                break;
            case R.id.updateplant:
                if ( !symptoms.getText().toString().isEmpty() && !description.getText().toString().isEmpty()
                        &&!information.getText().toString().isEmpty()  ){
                    if (mImageUri != null) {
                        mStorageRef = mStorageRef.child(name1
                                + "." + getFileExtension(mImageUri));
                        mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri downloadUrl = uri;

                                        reference = FirebaseDatabase.getInstance().getReference("plants");
                                        reference.child(name1).child("symptoms").setValue(symptoms.getText().toString());
                                        reference.child(name1).child("description").setValue(description.getText().toString());
                                        reference.child(name1).child("information").setValue(information.getText().toString());
                                        reference.child(name1).child("plant_image").setValue(downloadUrl.toString());

                                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("viewplant", MODE_PRIVATE).edit();
                                        editor.putString("name", name1);
                                        editor.putString("Symptoms", symptoms.getText().toString());
                                        editor.putString("description",description.getText().toString());
                                        editor.putString("information", information.getText().toString());
                                        editor.putString("plant_image", downloadUrl.toString());
                                        editor.apply();
                                        startActivity(new Intent(update_plant.this, view_plant.class));
                                    }
                                });
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(update_plant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                });
                    }

                }
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
            Glide.with(update_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_image);

        }
        else  if (requestCode == REQUEST_TAKE_PHOTO) {
            Glide.with(update_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_image);


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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(update_plant.this);
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
        if (ContextCompat.checkSelfPermission(update_plant.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(update_plant.this, new String[]{
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