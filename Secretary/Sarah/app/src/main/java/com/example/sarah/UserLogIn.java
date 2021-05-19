package com.example.sarah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout email1, password1;
    Button userSignUp;
    Button login1;
    ProgressDialog mProgress;
    Button fpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userSignUp = findViewById(R.id.signup);
        login1 = findViewById(R.id.login);
        email1 = findViewById(R.id.email_id);
        password1 = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        fpass = findViewById(R.id.fp);
        mProgress = new ProgressDialog(this);

        LinearLayout linearLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable)linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();




        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogIn.this,UserSignUp.class);
                startActivity(intent);

            }
        });

        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogIn.this,selection.class);
                startActivity(intent);

            }
        });

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email1.getEditText().getText().toString().trim();
                String passwd1 = password1.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    email1.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(passwd1))
                {
                    password1.setError("Password is required");
                }
                if(passwd1.length() <6)
                {
                    password1.setError("Password must be more than 6 characters ");
                }


                mProgress.setMessage("Logging In");
                mProgress.show();

                //progressBar.setVisibility(View.VISIBLE);


                mAuth.signInWithEmailAndPassword(email, passwd1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UserLogIn.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            Intent intent = new Intent(UserLogIn.this,Dashboard.class);
                            startActivity(intent);
                        } else
                        {
                            Toast.makeText(UserLogIn.this, "Error!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });




    }
}