package com.example.lmsnew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        // Using Handler to delay the start of MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity
                Intent mainIntent = new Intent(FirstPageActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // Finish current activity
            }
        }, SPLASH_TIMEOUT);
    }
}
