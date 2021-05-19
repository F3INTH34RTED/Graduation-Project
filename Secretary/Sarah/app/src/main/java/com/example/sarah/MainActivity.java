package com.example.sarah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static String SPLASH_SCREEN = "5000";

    Animation f_anim, l_anim;
    ImageView image;
    TextView logo, slogan;
    private VideoView video;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        f_anim = AnimationUtils.loadAnimation(this, R.anim.f_animation);
        l_anim = AnimationUtils.loadAnimation(this, R.anim.l_animation);

        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        video = findViewById(R.id.videobg);

        image.setAnimation(f_anim);
        logo.setAnimation(l_anim);
        slogan.setAnimation(l_anim);

        Uri uri = Uri.parse("android.resource://"
                + getPackageName()
                + "/"
                + R.raw.videobg);
        video.setVideoURI(uri);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer = mp;
                mMediaPlayer.setLooping(true);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, UserLogIn.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logo,"app_name");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent, options.toBundle());
                }


            }
        }, Long.parseLong(SPLASH_SCREEN));


    }
    @Override
    protected void onResume() {
        video.resume();

        super.onResume();
    }
    @Override
    protected void onPause(){
        video.suspend();
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        video.stopPlayback();
        super.onDestroy();
    }
}
