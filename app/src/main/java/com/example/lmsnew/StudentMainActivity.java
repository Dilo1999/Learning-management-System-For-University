package com.example.lmsnew;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        Button feedbackButton = findViewById(R.id.button5);

        // Set an OnClickListener on the button
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the FeedbackFormActivity
                Intent intent = new Intent(StudentMainActivity.this, FillFeedbackForm.class);
                startActivity(intent);
            }
        });
    }
}
