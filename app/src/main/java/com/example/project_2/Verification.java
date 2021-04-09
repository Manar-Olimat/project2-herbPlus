package com.example.project_2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Verification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

       /* try {
            GMailSender sender = new GMailSender("Project.herb2@gmail.com", "1234");
            sender.sendMail("test",
                    "This is test code",
                    "Project.herb2@gmail.com",
                    "Project.herb2@gmail.com");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }*/

    }
}