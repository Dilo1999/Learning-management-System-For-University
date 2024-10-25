package com.example.lmsnew;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.text.TextUtils;
import android.view.View;

import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


public class LectureLoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public static String lecsigninemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_login);


        emailEditText = findViewById(R.id.Lecture_login_email);
        passwordEditText = findViewById(R.id.Lecture_login_password);
        loginButton = findViewById(R.id.Lecture_login_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                lecsigninemail=email;
                if (validateInputs(email, password)) {
                    loginUser(email, password);
                }
            }
        });

    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return false;
        }

        return true;
    }

    private void loginUser(final String email, final String password) {
        db.collection("lectures").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && document.getString("password").equals(password)) {
                                Toast.makeText(LectureLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                //lecsigninemail=email;
                                Intent intent = new Intent(LectureLoginActivity.this, LectureMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LectureLoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LectureLoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}