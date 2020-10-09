package com.example.largeimageview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LargeImageView largeImageView = findViewById(R.id.large_view);
        try {
            InputStream inputStream = getAssets().open("reba.jpg");
            largeImageView.setImageResource(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}