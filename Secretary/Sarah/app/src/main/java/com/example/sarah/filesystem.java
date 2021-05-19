package com.example.sarah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class filesystem extends AppCompatActivity {

    private WebView webView;
    // callbacks to handle intent data returned from filechooser
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filesystem);
        requestAppPermissions();
        webView = (WebView) findViewById(R.id.webview);
        WebSettings mWebSettings = webView.getSettings();
        // to stay in the webview for redirecting links
        webView.setWebViewClient(new WebViewClient());
        mWebSettings.setJavaScriptEnabled(true);
        // disable zoom for webview, may cause glitches
        mWebSettings.setSupportZoom(false);
        // for uploading files
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowContentAccess(true);
        // load webview with the root url
        webView.loadUrl("http://40373b74efd2.ngrok.io");
        webView.setWebChromeClient(new WebChromeClient() {
            // handle file picker in webview
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                // new intent for choosing content
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                // wildcard for all file types
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                intentArray = new Intent[0];
                // choose the actual file
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                // choose content and initial intents of the intent array
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                // opens the file chooser activity
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
        // listener for a link redirecting to download in webview
        webView.setDownloadListener(new DownloadListener() {
            // handles the uri which redirects to the file uri in node server
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    // new intent to be started for the uri to be opened with a browser
                    getApplicationContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // if chrome is not installed, allow user to choose instead
                    intent.setPackage(null);
                    getApplicationContext().startActivity(intent);
                }
            }
        });

        ConstraintLayout constraintLayout = findViewById(R.id.filesystemsc);
        AnimationDrawable animationDrawable = (AnimationDrawable)constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);

        bottomNavigationView.setSelectedItemId(R.id.filesystemsc);

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
    }

    // handles the intent of file chooser for handling call back when the activity is swtiched from the current app to the file picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
//                if (null == mUM) return;
                Uri result = intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    // get permission as the function name says
    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }
        // Requests permission for External Storage
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 101); // your request code
    }

    // checks whether the app already has 'READ' permission as the name suggests
    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    // checks whether the app already has 'WRITE' permission as the name suggests
    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}
