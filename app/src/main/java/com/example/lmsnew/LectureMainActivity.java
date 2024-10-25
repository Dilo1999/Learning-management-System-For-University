package com.example.lmsnew;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LectureMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_main);

        Button feedbackButton = findViewById(R.id.button6);
        Button viewFeedbackButton = findViewById(R.id.button7);

        // Set an OnClickListener on the button
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the FeedbackFormActivity
                Intent intent = new Intent(LectureMainActivity.this,CreateFeedbackForm.class);
                startActivity(intent);
            }
        });
        // Set an OnClickListener on the button
        viewFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the FeedbackFormActivity
                Intent intent = new Intent(LectureMainActivity.this,View_feedback_form.class);
                startActivity(intent);
            }
        });
    }
}
