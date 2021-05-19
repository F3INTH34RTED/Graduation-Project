package com.example.sarah;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

public class summarize extends AppCompatActivity {

    TextView textView,textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_summarize);
        textView1 = findViewById(R.id.text_view_id);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showSummary();
            }
        }, 5 * 1000);
    }

    //get file from FirebaseStorage then store in device then read to view it
    private void showSummary()  {
        File outputfile = new File(Environment.getExternalStorageDirectory(), "summary.txt");

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(outputfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        String str = text.toString();
        str.replaceAll("\\.\\u2022", "\\.\n");
        textView1.setTextSize(15);
        textView1.setText(str);
    }


}


