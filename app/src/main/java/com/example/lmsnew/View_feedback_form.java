package com.example.lmsnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class View_feedback_form extends AppCompatActivity {

    private Spinner spinnerLecturers;
    private RecyclerView rvAnswers;
    private FirebaseFirestore db;
    private AnswerAdapter answerAdapter;
    private List<Answer> answerList;
    private String selectedStudentEmail;

    private String lecturerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback_form);

        spinnerLecturers = findViewById(R.id.spinnerLecturers);
        rvAnswers = findViewById(R.id.rvAnswers);
        db = FirebaseFirestore.getInstance();
        answerList = new ArrayList<>();
        answerAdapter = new AnswerAdapter(answerList);

        rvAnswers.setLayoutManager(new LinearLayoutManager(this));
        rvAnswers.setAdapter(answerAdapter);
        Log.d("", "First check ");

        loadStudentEmails();
    }

    private void loadStudentEmails() {
        db.collection("StudentAnswers").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> studentEmails = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String email = document.getString("StudentEmail");
                        lecturerEmail = document.getString("lecturerEmail");
                        if (LectureLoginActivity.lecsigninemail != null && LectureLoginActivity.lecsigninemail.equals(lecturerEmail) && email != null && !studentEmails.contains(email)) {
                            studentEmails.add(email);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentEmails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLecturers.setAdapter(adapter);
                    spinnerLecturers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedStudentEmail = studentEmails.get(position);
                            loadAnswers();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedStudentEmail = null;
                        }
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load student emails", Toast.LENGTH_SHORT).show());
    }

    private void loadAnswers() {
        if (selectedStudentEmail == null) {
            Toast.makeText(this, "Please select a student", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("StudentAnswers")
                .whereEqualTo("lecturerEmail", lecturerEmail)
                .whereEqualTo("StudentEmail", selectedStudentEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    answerList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            List<Map<String, Object>> answers = (List<Map<String, Object>>) documentSnapshot.get("answers");
                            if (answers != null) {
                                for (Map<String, Object> answerMap : answers) {
                                    String question = (String) answerMap.get("question");
                                    String answer = (String) answerMap.get("answer");
                                    answerList.add(new Answer(question, answer));
                                }
                                answerAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(this, "No answers found for the selected student", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, "No document found for the selected student", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load answers: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
