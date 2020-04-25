package com.example.popular_movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.popular_movies.R;


public class WebViewActivity extends AppCompatActivity {

    String url;
    public static String URL_OF_REVIEW = "url";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_web_view);

        final ProgressBar loadingIndicator = findViewById(R.id.indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        WebView web = findViewById(R.id.webView);

        Intent intent = getIntent();
        url = intent.getStringExtra(URL_OF_REVIEW);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(url);

        web.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }


}
