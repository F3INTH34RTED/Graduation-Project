package com.example.sarah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class selection extends AppCompatActivity {

    Button esms;
    Button msms;
    ImageView b0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        esms = findViewById(R.id.email);
        msms = findViewById(R.id.sms);
        b0 = findViewById(R.id.back0);

        msms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selection.this,forgetpassSMS.class);
                startActivity(intent);

            }
        });

        esms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selection.this,forgetPassword.class);
                startActivity(intent);

            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selection.this,UserLogIn.class);
                startActivity(intent);

            }
        });
    }
}