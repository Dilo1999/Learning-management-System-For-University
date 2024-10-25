package com.example.lmsnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Button buttonCreateStudentAccount = findViewById(R.id.button);
        Button buttonCreateLectureAccount = findViewById(R.id.button2);
        Button buttonremoveStudentAccount = findViewById(R.id.button3);
        Button buttonremoveLectureAccount = findViewById(R.id.button4);

        buttonCreateStudentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, Create_Student_Account.class);
                startActivity(intent);
            }
        });

        buttonCreateLectureAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, Create_Lecture_Account.class);
                startActivity(intent);
            }
        });
        buttonremoveStudentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, DeleteStuAccount.class);
                startActivity(intent);
            }
        });
        buttonremoveLectureAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, DeleteLecAccount.class);
                startActivity(intent);
            }
        });
    }
}
