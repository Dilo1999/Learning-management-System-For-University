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
import com.google.firebase.auth.AuthResult;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText emailStudent, passwordStudent;
    private Button loginButtonStu;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public static String stuname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        emailStudent = findViewById(R.id.Student_login_email);
        passwordStudent = findViewById(R.id.Student_login_password);
        loginButtonStu = findViewById(R.id.Student_login_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginButtonStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailStudent.getText().toString().trim();
                String password = passwordStudent.getText().toString().trim();

                if (validateInputs(email, password)) {
                    loginUser(email, password);
                }
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailStudent.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordStudent.setError("Password is required");
            return false;
        }

        return true;
    }

    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(StudentLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            stuname=emailStudent.getText().toString().trim();
                            // Navigate to StudentMainActivity
                            Intent intent = new Intent(StudentLoginActivity.this, StudentMainActivity.class);
                            startActivity(intent);
                            finish(); // Optional: finish the login activity so the user can't go back to it
                        } else {
                            Toast.makeText(StudentLoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
