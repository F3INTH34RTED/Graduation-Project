package com.example.sarah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {

    ImageView b3;
    TextInputLayout t;
    FirebaseAuth fAuth;
    Button reset1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        b3 = findViewById(R.id.back3);
        fAuth = FirebaseAuth.getInstance();
        reset1 = findViewById(R.id.emailreset);
        t = findViewById(R.id.remail);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgetPassword.this,selection.class);
                startActivity(intent);

            }
        });

        reset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rmail = t.getEditText().getText().toString();
                fAuth.sendPasswordResetEmail(rmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(forgetPassword.this, "Reset password send to mail", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(forgetPassword.this, "Error! Email could not be send" +e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }
}