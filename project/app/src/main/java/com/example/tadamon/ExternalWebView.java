package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class ExternalWebView extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_web_view);

        webView = findViewById(R.id.webview);
        webView.loadUrl("https://forms.gle/rQrA3EJp1TvQjf9JA");
    }
}