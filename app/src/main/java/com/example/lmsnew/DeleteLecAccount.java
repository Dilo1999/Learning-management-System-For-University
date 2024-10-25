package com.example.lmsnew;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteLecAccount extends AppCompatActivity {

    private Spinner studentSpinner;
    private Button deleteButton;
    private FirebaseFirestore db;
    private List<String> studentEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_lec_account);

        studentSpinner = findViewById(R.id.lectures_spinner);
        deleteButton = findViewById(R.id.lectures_delete_button);
        db = FirebaseFirestore.getInstance();

        loadStudents();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedEmail = (String) studentSpinner.getSelectedItem();
                if (selectedEmail != null && !selectedEmail.isEmpty()) {
                    deleteStudent(selectedEmail);
                } else {
                    Toast.makeText(DeleteLecAccount.this, "No lectures selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadStudents() {
        db.collection("lectures")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            studentEmails = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                studentEmails.add(document.getId());
                            }
                            if (!studentEmails.isEmpty()) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(DeleteLecAccount.this, android.R.layout.simple_spinner_item, studentEmails);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                studentSpinner.setAdapter(adapter);
                            } else {
                                Toast.makeText(DeleteLecAccount.this, "No students found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("FirestoreError", "Error getting students: ", task.getException());
                            Toast.makeText(DeleteLecAccount.this, "Error getting students: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteStudent(String email) {
        db.collection("lectures").document(email)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DeleteLecAccount.this, "lectures deleted", Toast.LENGTH_SHORT).show();
                            loadStudents(); // Refresh the list
                        } else {
                            Log.e("FirestoreError", "Error deleting student: ", task.getException());
                            Toast.makeText(DeleteLecAccount.this, "Error deleting student: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
