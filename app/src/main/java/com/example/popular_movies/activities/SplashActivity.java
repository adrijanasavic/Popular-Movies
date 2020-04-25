package com.example.popular_movies.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.popular_movies.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_TIMEOUT = 2000;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_splash );
        imageView = findViewById( R.id.imageSplash );
        InputStream is;
        try {
            is = getAssets().open( "android_pink.jpg" );
            Drawable drawable = Drawable.createFromStream( is, null );
            imageView.setImageDrawable( drawable );
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Timer().schedule( new TimerTask() {
            @Override
            public void run() {
                startActivity( new Intent( SplashActivity.this, MainActivity.class ) );
                finish();
            }
        }, SPLASH_TIMEOUT );
    }
}
