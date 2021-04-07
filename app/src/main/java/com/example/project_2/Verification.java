package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class Verification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        try {
            GMailSender sender = new GMailSender("shathaolaimat@gmail.com", "1234");
            sender.sendMail("test",
                    "This is test code",
                    "shathaolaimat@gmail.com",
                    "shathaolaimat@gmail.com");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }

    }
}