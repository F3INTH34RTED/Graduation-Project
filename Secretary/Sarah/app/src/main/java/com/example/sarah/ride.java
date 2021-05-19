package com.example.sarah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;
import java.util.Arrays;
import com.uber.sdk.android.rides.RideRequestButton;

public class ride extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);

        bottomNavigationView.setSelectedItemId(R.id.ridesc);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext()
                                ,Dashboard.class));
                        overridePendingTransition(0,0);
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

        ConstraintLayout constraintLayout = findViewById(R.id.ridesc);
        AnimationDrawable animationDrawable = (AnimationDrawable)constraintLayout.getBackground();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            animationDrawable.setEnterFadeDuration(2000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            animationDrawable.setExitFadeDuration(4000);
        }
        animationDrawable.start();

        SessionConfiguration sessionConfiguration = new SessionConfiguration.Builder()
                .setClientId("u-ARZTkzd1DD4Hf5Ay3h_KIek4o8toc7")
                //.setServerToken("")
               // .setRedirectUri("")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                .setEnvironment(SessionConfiguration.Environment.PRODUCTION)
                .build();
        UberSdk.initialize(sessionConfiguration);

        RideRequestButton requestButton = new RideRequestButton(ride.this);
        ConstraintLayout layout = new ConstraintLayout(this);
        layout.addView(requestButton);
    }
}
