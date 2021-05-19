package com.example.sarah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class reset extends AppCompatActivity {

    Button r;
    ImageView b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        r = findViewById(R.id.reset);
        b1 = findViewById(R.id.back1);

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reset.this,back2logIn.class);
                startActivity(intent);

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reset.this,forgetPassword.class);
                startActivity(intent);

            }
        });
    }
}