package com.example.project_2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.project_2.Models.userDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class add_plant extends AppCompatActivity implements View.OnClickListener{
    MaterialToolbar back;
    EditText plantame;
    TextView symptoms;
    MultiAutoCompleteTextView description, information;
    Button addplant;
    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray={"Fatigue","Fever","Stomachache","Headache","Nausea","Skin irritation",
            "Indigestion","Infections and ulcers","Diarrhea","Constipation","Colds"};

    FirebaseDatabase root;
    FirebaseUser user;
    FirebaseAuth mAuth ;
    DatabaseReference reference,reference_user;
    String userID;

    FloatingActionButton add_img;
    ImageView plant_img;
    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri=null;
    private StorageReference mStorageRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        plantame =findViewById(R.id.plantname);
        symptoms = findViewById(R.id.symptoms);
        description=findViewById(R.id.description);
        information=findViewById(R.id.information);
        addplant=findViewById(R.id.addplant);
        back=findViewById(R.id.topAppBar);
        add_img=findViewById(R.id.add_img);
        plant_img=findViewById(R.id.plant_img);
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("plants");

        user= FirebaseAuth.getInstance().getCurrentUser();
        selectedsymptoms = new boolean[symptomsArray.length];
        symptoms.setOnClickListener(this);
        back.setOnClickListener(this);
        add_img.setOnClickListener(this);
        addplant.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.symptoms:
                AlertDialog.Builder builder = new AlertDialog.Builder(add_plant.this);
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

            case R.id.back:
                startActivity(new Intent(add_plant.this, home.class));
                break;
            case R.id.add_img:
                selectImage();
                break;
            case R.id.addplant:
                checkDataEntered();
                if (!plantame.getText().toString().isEmpty() && !symptoms.getText().toString().isEmpty() &&
                        !description.getText().toString().isEmpty() &&!information.getText().toString().isEmpty()  ){
                    final String plantnameValue = plantame.getText().toString();
                    final String symptomsValue = symptoms.getText().toString();
                    final String descriptionValue = description.getText().toString();
                    final String informationValue = information.getText().toString();

                    userID=user.getUid();
                    reference_user= FirebaseDatabase.getInstance().getReference("users");
                    reference_user.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            com.example.project_2.Models.userDB userProfile=snapshot.getValue(userDB.class);
                            if(userProfile.getAccountType().equals("Admin Account")){
                                reference = root.getReference("plants");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(plantnameValue)) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(add_plant.this);
                                            builder1.setMessage("The plant already exists. Do you want to modify the data?");
                                            builder1.setCancelable(false);

                                            builder1.setPositiveButton(
                                                    "Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            if (mImageUri != null) {
                                                                mStorageRef = mStorageRef.child(plantnameValue
                                                                        + "." + getFileExtension(mImageUri));
                                                                mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                final Uri downloadUrl = uri;
                                                                                if(downloadUrl!=null){
                                                                                    reference = root.getReference("plants").child(plantnameValue);
                                                                                    reference.child("name").setValue(plantnameValue);
                                                                                    reference.child("symptoms").setValue(symptomsValue);
                                                                                    reference.child("description").setValue(descriptionValue);
                                                                                    reference.child("information").setValue(informationValue);
                                                                                    reference.child("plant_image").setValue(downloadUrl.toString());

                                                                                    Intent mainIntent = new Intent(add_plant.this, home.class);
                                                                                    add_plant.this.startActivity(mainIntent);
                                                                                }else {
                                                                                    Toast.makeText(add_plant.this, "No file selected", Toast.LENGTH_SHORT).show();

                                                                                }

                                                                                }
                                                                            });

                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(add_plant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                                            }
                                                                        });
                                                            }

                                                        }
                                                    });

                                            builder1.setNegativeButton(
                                                    "No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                        } else {

                                            if (mImageUri != null) {
                                                mStorageRef = mStorageRef.child(plantnameValue
                                                        + "." + getFileExtension(mImageUri));
                                                mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                final Uri downloadUrl = uri;

                                                                if(downloadUrl!=null){
                                                                    reference = root.getReference("plants").child(plantnameValue);
                                                                    reference.child("name").setValue(plantnameValue);
                                                                    reference.child("symptoms").setValue(symptomsValue);
                                                                    reference.child("description").setValue(descriptionValue);
                                                                    reference.child("information").setValue(informationValue);
                                                                    reference.child("plant_image").setValue(downloadUrl.toString());

                                                                    Intent mainIntent = new Intent(add_plant.this, home.class);
                                                                    add_plant.this.startActivity(mainIntent);
                                                                }else {
                                                                    Toast.makeText(add_plant.this, "No file selected", Toast.LENGTH_SHORT).show();

                                                                }

                                                            }
                                                        });

                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(add_plant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                            }
                                                        });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(add_plant.this);
                                builder1.setMessage("The plant has been add but it need for the admin confirmation...");
                                builder1.setCancelable(false);
                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (mImageUri != null) {
                                                    mStorageRef = mStorageRef.child(plantnameValue
                                                            + "." + getFileExtension(mImageUri));
                                                    mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    final Uri downloadUrl = uri;

                                                                    if(downloadUrl!=null){
                                                                        Date date = new Date();
                                                                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                                                        reference = root.getReference("confirm_plants").child(plantnameValue);
                                                                        reference.child("name").setValue(plantnameValue);
                                                                        reference.child("symptoms").setValue(symptomsValue);
                                                                        reference.child("description").setValue(descriptionValue);
                                                                        reference.child("information").setValue(informationValue);
                                                                        reference.child("added_by").setValue(userProfile.getUsername());
                                                                        reference.child("plant_image").setValue(downloadUrl.toString());
                                                                        reference.child("date").setValue(localDate.getMonthValue()+"/"+ localDate.getDayOfMonth()+"/"+ localDate.getYear());

                                                                        Intent mainIntent = new Intent(add_plant.this, home.class);
                                                                        add_plant.this.startActivity(mainIntent);
                                                                    }else {
                                                                        Toast.makeText(add_plant.this, "No file selected", Toast.LENGTH_SHORT).show();

                                                                    }

                                                                }
                                                            });

                                                        }
                                                    })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(add_plant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                                }
                                                            });
                                                }

                                            }
                                        });

                                builder1.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                break;
        }
    }
    private void checkDataEntered() {
        if (isEmpty(plantame)) {
            plantame.setError("Empty plant name");
        }
        if ( isEmpty(symptoms)) {
            symptoms.setError("Empty symptoms");

        }
        if (isEmpty(description)) {
            description.setError("Empty description");
        }
        if (isEmpty(information)) {
            information.setError("Empty information");
        }


    }

    boolean isEmpty( EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmpty( TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }boolean isEmpty( MultiAutoCompleteTextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
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
            Glide.with(add_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_img);

        }
        else  if (requestCode == REQUEST_TAKE_PHOTO) {
            Glide.with(add_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_img);


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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(add_plant.this);
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
        if (ContextCompat.checkSelfPermission(add_plant.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(add_plant.this, new String[]{
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