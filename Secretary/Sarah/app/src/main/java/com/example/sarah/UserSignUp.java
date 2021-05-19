package com.example.sarah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class UserSignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button login, signup;
    TextInputLayout uemail, upass, firstname, username, ph;
    //ProgressBar progressBar;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_sign_up);

        LinearLayout linearLayout = findViewById(R.id.signupscreen);
        AnimationDrawable animationDrawable = (AnimationDrawable)linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        login = findViewById(R.id.e_login);
        signup = findViewById(R.id.usersignup);
        uemail = findViewById(R.id.user_mail);
        upass = findViewById(R.id.user_password);
        firstname = findViewById(R.id.f_name);
        username = findViewById(R.id.user_name);
        ph = findViewById(R.id.user_phone);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        if(mAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(UserSignUp.this,Dashboard.class);
            startActivity(intent);

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSignUp.this,UserLogIn.class);
                startActivity(intent);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = firstname.getEditText().getText().toString().trim();
                String usernamee = username.getEditText().getText().toString().trim();
                String phone = ph.getEditText().getText().toString().trim();

                String email = uemail.getEditText().getText().toString().trim();
                String passwd = upass.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    uemail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(passwd))
                {
                    upass.setError("Password is required");
                }
                if(TextUtils.isEmpty(fname))
                {
                    firstname.setError("Cannot be empty");
                }
                if(TextUtils.isEmpty(usernamee))
                {
                    username.setError("Cannot be empty");
                }
                if(TextUtils.isEmpty(phone))
                {
                    ph.setError("Cannot be empty");
                }

                if(passwd.length() <6)
                {
                    upass.setError("Password must be more than 6 characters ");
                }
                //progressBar.setVisibility(View.VISIBLE);

                mProgress.setMessage("Hold on we are creating your account");
                mProgress.show();


                mAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UserSignUp.this, "User created", Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            //navigation to the dashboard page
                            Intent intent = new Intent(UserSignUp.this,Dashboard.class);
                            startActivity(intent);

                        }else
                        {
                            Toast.makeText(UserSignUp.this, "Error!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });






            }
        });



    }
}
