/**
 * This program creates Search for nerby restraunts
 * CPSC 312-01, Fall 2019
 * Programming Assignment #8
 * No sources to cite.
 *
 * @author Jakob Kubicci and Ahmad Moltafet
 * @version v1.0 12/10/21
 */

package com.jkam.pa8;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;


public class PlaceDetailActivity extends AppCompatActivity {

    String imageUrl;

    /**
     Each place details
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        TextView titleView = findViewById(R.id.titleView);
        TextView phoneView = findViewById(R.id.phoneView);
        ImageView imageView = findViewById(R.id.imageView);
        TextView body = findViewById(R.id.body);
        Intent intent = getIntent();
        if (intent != null) {
            imageUrl = intent.getStringExtra("url");
            String importedData = intent.getStringExtra("address")
                    + intent.getStringExtra("name");
            titleView.setText(intent.getStringExtra("name") + " ");
            phoneView.setText(intent.getStringExtra("phone"));
            imageView.setImageDrawable(LoadImageFromWebOperations(imageUrl));
            body.setText(importedData);
        }
    }

    /**
     Loads the image
     *
     * @param url
     * @return Drawable
     */
   public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
           return null;
       }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
