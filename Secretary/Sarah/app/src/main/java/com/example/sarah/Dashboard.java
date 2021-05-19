package com.example.sarah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class Dashboard extends AppCompatActivity {

    ImageButton nm, fs, c, m, r, ca;
    ImageView IMG;
    ConstraintLayout cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ConstraintLayout constraintLayout = findViewById(R.id.dash);
        AnimationDrawable animationDrawable = (AnimationDrawable)constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        nm = findViewById(R.id.notemaker);
        fs = findViewById(R.id.filesystem);
        c = findViewById(R.id.contacts);
        m = findViewById(R.id.mail);
        r = findViewById(R.id.uber);
        ca = findViewById(R.id.calendar);
       // IMG = findViewById(R.id.image1);
        cl = findViewById(R.id.dash);


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);

        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.dashboard:
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                ,settings.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.lexi_chatbot:
                        startActivity(new Intent(getApplicationContext()
                                ,Lexi_chatbot.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        nm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,notemaker.class);
                startActivity(intent);

            }
        });

        fs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,filesystem.class);
                startActivity(intent);

            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Dashboard.this,contacts.class);
               // startActivity(intent);
                //Intent i = new Intent();
                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.contacts");
                i.setAction("android.intent.action.MAIN");
                i.addCategory("android.intent.category.LAUNCHER");
                i.addCategory("android.intent.category.DEFAULT");
                startActivity(i);

            }
        });

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                startActivity(intent);

            }
        });

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,ride.class);
                startActivity(intent);

            }
        });

        ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);

            }
        });

        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(Dashboard.this).withPermissions(Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()){
                            Toast.makeText(Dashboard.this, "All permissions are granted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Dashboard.this, "All permissions are not granted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
            }
        });






    }






}
