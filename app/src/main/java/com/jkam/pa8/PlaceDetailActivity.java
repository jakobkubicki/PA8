package com.jkam.pa8;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;


public class PlaceDetailActivity extends AppCompatActivity {

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        TextView titleView = findViewById(R.id.titleView);
        TextView body = findViewById(R.id.body);
        Intent intent = getIntent();
        if (intent != null) {
            imageUrl = intent.getStringExtra("url");
            String importedData = intent.getStringExtra("address")
                    + intent.getStringExtra("name");
            titleView.setText(intent.getStringExtra("name"));
            body.setText(importedData);
        }
    }

//    public static Drawable LoadImageFromWebOperations(String url) {
//        try {
//            InputStream is = (InputStream) new URL(url).getContent();
//            Drawable d = Drawable.createFromStream(is, "src name");
//            return d;
//        } catch (Exception e) {
//            return null;
//        }
//    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
