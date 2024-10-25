package com.example.lmsnew;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Create_Student_Account extends AppCompatActivity {

    private EditText emailstudent, passwordstudent;
    private Button createButtonstu;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student_account);

        emailstudent = findViewById(R.id.Student_AC_create);
        passwordstudent = findViewById(R.id.Student_AC_create_password);
        createButtonstu = findViewById(R.id.Student_AC_create_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        createButtonstu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailstudent.getText().toString().trim();
                String password = passwordstudent.getText().toString().trim();

                if (validateInputs(email, password)) {
                    createUser(email, password);
                }
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailstudent.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordstudent.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            createButtonstu.setError("Password must be >= 6 characters");
            return false;
        }

        return true;
    }

    private void createUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User created, add data to Firestore
                            addUserToFirestore(email, password);
                        } else {
                            Toast.makeText(Create_Student_Account.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToFirestore(String email, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);

        // Use the email as the document ID
        db.collection("students")
                .document(email)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Create_Student_Account.this, "Student added to Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Create_Student_Account.this, "Failed to add user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
