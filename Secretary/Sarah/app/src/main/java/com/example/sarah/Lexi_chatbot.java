package com.example.sarah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Lexi_chatbot extends AppCompatActivity {

    private EditText editText;
    private final int USER = 0;
    private final int BOT = 1;
    protected static final int RESULT_SPEECH = 1;
    private ImageButton buttonSpeak;
    private TextView tvText;
    private TextToSpeech mtts; // Text to speech object


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lexi_chatbot);
        editText = findViewById(R.id.edittext_chatbox);


        ConstraintLayout constraintLayout = findViewById(R.id.chatbotsc);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);

        bottomNavigationView.setSelectedItemId(R.id.chatbotsc);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext()
                                , Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                , settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.lexi_chatbot:
                        return true;
                }
                return false;
            }
        });
        tvText = findViewById(R.id.tvText);
        buttonSpeak = findViewById(R.id.ButtonSpeak);
        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "Your device does not support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(text.get(0));
                }
                break;
        }
    }

    private void searchNet(String words) {
        try {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, words);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
            searchNetCompat(words);
        }
    }
    // search internet with the browser if there is no search app
    private void searchNetCompat(String words) {
        try {
            Uri uri = Uri.parse("http://www.google.com/#q="+words);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(View view) {
        UserMessage userMessage = null;
        MessageSender messageSender;
        String msg = editText.getText().toString();
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://c2ce47f516b1.ngrok.io/webhooks/rest/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (msg.trim().isEmpty()) {
            Toast.makeText(Lexi_chatbot.this, "Please enter your query!", Toast.LENGTH_LONG).show();
        } else {
            showTextView(msg, USER);
            editText.setText("");

            userMessage = new UserMessage("User",msg);
        }
//         Toast.makeText(Lexi_chatbot.this, ""+userMessage.getMessage(), Toast.LENGTH_LONG).show();
        messageSender = retrofit.create(MessageSender.class);
        Call<List<BotResponse>> response = messageSender.sendMessage(userMessage);
        response.enqueue(new Callback<List<BotResponse>>() {
            @Override
            public void onResponse(Call<List<BotResponse>> call, Response<List<BotResponse>> response) {
                if(response.body() == null || response.body().size() == 0){
                    showTextView("Sorry didn't understand",BOT);
                }
                else{
                    BotResponse botResponse = response.body().get(0);
                    String bot_response = botResponse.getText().toString();
                    String lastTwo = null;
                    if (bot_response != null && bot_response.length() >= 2) {
                        lastTwo = bot_response.substring(bot_response.length() - 2);
                    }
                    Intent intent;
                    switch (lastTwo)
                    {
                        case "/c":
                            intent = new Intent(Lexi_chatbot.this, filesystem.class);
                            startActivity(intent);
                            break;
                        case "/g":
                            String searchTerms = bot_response.substring(0, bot_response.length() - 2);;
                            if (!searchTerms.equals("")) {
                                searchNet(searchTerms);
                            }
                            break;
                        case "/r":  // open recording page
                        case "/n": // open summarize page
                            intent = new Intent(Lexi_chatbot.this, notemaker.class);
                            startActivity(intent);
                            break;
                        case "/t": // open Uber page
                            intent = new Intent(Lexi_chatbot.this, ride.class);
                            startActivity(intent);
                            break;
                        case "/d": // open calendar page
                            intent = getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
                            intent.setAction("android.intent.action.MAIN");
                            intent.addCategory("android.intent.category.LAUNCHER");
                            intent.addCategory("android.intent.category.DEFAULT");
                            startActivity(intent);
                            break;
                        case "/p": // open contacts page
                            intent = new Intent();
                            intent.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
                            intent.setAction("android.intent.action.MAIN");
                            intent.addCategory("android.intent.category.LAUNCHER");
                            intent.addCategory("android.intent.category.DEFAULT");
                            startActivity(intent);
                            break;
                        case "/e": // open email page
                            intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                            startActivity(intent);
                            break;
                        default:
                            showTextView(botResponse.getText(),BOT);
                            break;
                    }

                }
            }
            @Override
            public void onFailure(Call<List<BotResponse>> call, Throwable t) {
                showTextView("Waiting for message",BOT);
                Toast.makeText(Lexi_chatbot.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });


    }
    private void showTextView(String message, int type) {
        LinearLayout chatLayout = findViewById(R.id.chat_layout);
        LayoutInflater inflater = LayoutInflater.from(Lexi_chatbot.this);
        FrameLayout layout;
        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                mtts = new TextToSpeech(Lexi_chatbot.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS)
                        {
                            int result = mtts.setLanguage(Locale.US);

                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                            {
                                Log.e("TTS","Language not supported!");
                                Toast.makeText(Lexi_chatbot.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                mtts.setPitch((float) 1);
                                mtts.setSpeechRate((float) 1);
                                mtts.speak(message, TextToSpeech.QUEUE_ADD, null);
                                Toast.makeText(Lexi_chatbot.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Log.e("TTS","Initialization Failed!");
                        }
                    }
                });
                break;
            default:
                layout = getBotLayout();
                break;
        }
        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout);
        TextView tv = layout.findViewById(R.id.chat_msg);
        tv.setText(message);
        layout.requestFocus();
        editText.requestFocus();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",
                Locale.ENGLISH);
        String time = dateFormat.format(date);
        TextView timeTextView = layout.findViewById(R.id.message_time);
        timeTextView.setText(time.toString());
    }

    FrameLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(Lexi_chatbot.this);
        return (FrameLayout) inflater.inflate(R.layout.user, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(Lexi_chatbot.this);
        return (FrameLayout) inflater.inflate(R.layout.bot, null);
    }

    @Override
    protected void onDestroy() {
        if (mtts != null) {
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }
}

