package com.example.sarah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class back2logIn extends AppCompatActivity {
    Button back2login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back2log_in);
        back2login = findViewById(R.id.b2lg);

        back2login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(back2logIn.this,UserLogIn.class);
                startActivity(intent);

            }
        });
    }
}