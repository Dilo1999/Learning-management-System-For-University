package com.example.lmsnew;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillFeedbackForm extends AppCompatActivity {

    private Spinner spinnerLecturers;
    private RecyclerView rvQuestions;
    private Button btnSubmitAnswers;
    private FirebaseFirestore db;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;
    private String selectedLecturerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_feedback_form);

        spinnerLecturers = findViewById(R.id.spinnerLecturers);
        rvQuestions = findViewById(R.id.rvQuestions);
        btnSubmitAnswers = findViewById(R.id.btnSubmitAnswers);
        db = FirebaseFirestore.getInstance();
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList);

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(questionAdapter);

        loadLecturers();
        btnSubmitAnswers.setOnClickListener(v -> submitAnswers());
    }

    private void loadLecturers() {
        db.collectionGroup("questions_list").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> lecturerEmails = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String email = document.getReference().getParent().getParent().getId();
                        if (!lecturerEmails.contains(email)) {
                            lecturerEmails.add(email);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lecturerEmails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLecturers.setAdapter(adapter);
                    spinnerLecturers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedLecturerEmail = lecturerEmails.get(position);
                            loadQuestions();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedLecturerEmail = null;
                        }
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load lecturers", Toast.LENGTH_SHORT).show());
    }

    private void loadQuestions() {
        if (selectedLecturerEmail == null) {
            Toast.makeText(this, "Please select a lecturer", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("questions").document(selectedLecturerEmail).collection("questions_list").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    questionList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Question question = document.toObject(Question.class);
                        questionList.add(question);
                        Log.d("TAG", "loadQuestions: ");
                    }
                    questionAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load questions", Toast.LENGTH_SHORT).show());
    }

    private void submitAnswers() {

        if (selectedLecturerEmail == null) {
            Toast.makeText(this, "Please select a lecturer", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Map<String, Object>> answersList = new ArrayList<>();
        for (int i = 0; i < rvQuestions.getChildCount(); i++) {
            View view = rvQuestions.getChildAt(i);
            EditText etAnswer = view.findViewById(R.id.etAnswer);
            String questionText = questionList.get(i).getQuestion(); // Assuming Question class has a getQuestionText method
            String answer = etAnswer.getText().toString().trim();

            Map<String, Object> answerMap = new HashMap<>();
            answerMap.put("question", questionText);
            answerMap.put("answer", answer);
            answersList.add(answerMap);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("answers", answersList);
        data.put("lecturerEmail", selectedLecturerEmail);
        data.put("StudentEmail",StudentLoginActivity.stuname);

        String documentName = selectedLecturerEmail+","+StudentLoginActivity.stuname; // Hardcoded document name

        db.collection("StudentAnswers").document(documentName).set(data)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Answers submitted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to submit answers", Toast.LENGTH_SHORT).show());
    }

}
