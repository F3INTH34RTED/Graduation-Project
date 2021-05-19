package com.example.sarah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static java.util.Objects.requireNonNull;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    File[] allFiles;
    private TimeAgo timeAgo;
    private summarize summarize;
    private NavController navController;
    Context context;
    private ArrayList<String> data;
    private onItemListClick onItemListClick;
    public TextView list_title;
    private StorageReference mStorage;
    private static String mFileName = null;
    int counter = 0;
    File fileEvents, mFile;
    private ProgressDialog mProgress;


    public AudioListAdapter(File[] allFiles, onItemListClick onItemListClick) {
        this.allFiles = allFiles;
        this.onItemListClick = onItemListClick;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        timeAgo = new TimeAgo();
        summarize = new summarize();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.list_title.setText(allFiles[position].getName());
        holder.list_date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));
        //holder.note_btn.setText(allFiles[position].getAbsoluteFile());

        holder.note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // v.setFocusable(false);
                showSummary();
                ConstraintLayout parent = (ConstraintLayout) v.getParent();
                list_title = null;
                if (parent.getChildAt(2) instanceof TextView) {
                    list_title = (TextView) parent.getChildAt(2);
                    String name = (String) list_title.getText();
                    Log.d("myTag", name);
                    Toast.makeText(v.getContext(), name, Toast.LENGTH_LONG).show();


                    fileEvents = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "File.txt");
                    try {
                        BufferedWriter fos = new BufferedWriter(new FileWriter(fileEvents));
                        fos.write(name);
                        fos.close();
                        Toast.makeText(v.getContext(), "Saved", Toast.LENGTH_LONG);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    }


                    Uri FileUri = Uri.fromFile(new File(String.valueOf(fileEvents)));
                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("text/plain")
                            .build();

                    mStorage = FirebaseStorage.getInstance().getReference();
                    StorageReference Folder = mStorage.child("Audio Name");
                    UploadTask up = Folder.putFile(FileUri, metadata);

                    up.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("New Audio Name Updated", name);
                        }
                    });
                }
                showSummary();


                mProgress = new ProgressDialog(v.getContext());
                mProgress.setMessage("Generating summary");
                mProgress.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            removeWorkingDialog();
                            Intent i = new Intent(v.getContext(), summarize.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("hi", showSummary());
                            // Log.d("WHAT I SENT: ", String.valueOf(i.putExtra("WHAT I SENT: ", showSummary())));
                            i.putExtras(bundle);
                            v.getContext().startActivity(i);
                        }
                    }, 25000);
            }
        });
    }


    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public void removeItem(int position) {
        allFiles[position].getAbsoluteFile().delete();
        notifyItemRemoved(position);
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private TextView list_title;
        private TextView list_date;
        private Button note_btn;


        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_image = itemView.findViewById(R.id.list_image_view);
            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
            note_btn = itemView.findViewById(R.id.summarize_btn);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListClick.onClickListener(allFiles[getAdapterPosition()], getAdapterPosition());
        }
    }

    public interface onItemListClick {
        void onClickListener(File file, int position);
    }

    //get file from FirebaseStorage then store in device then read to view it
    private String showSummary() {
        StorageReference sr = FirebaseStorage.getInstance().getReference();
        File outputfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +"summary.txt");
        sr.child("Summarized Text").getFile(outputfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "File Downloaded: " + outputfile);
            }
        });

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
        return str;
    }

    private void removeWorkingDialog() {
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
    }

}



