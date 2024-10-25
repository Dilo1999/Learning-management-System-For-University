package com.example.lmsnew;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CreateFeedbackForm extends AppCompatActivity {
    private Button btnAddQuestion;
    private Button btnRemoveQuestion;
    private RecyclerView rvQuestions;
    private FirebaseFirestore db;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;
    private int questionNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feedback_form);

        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnRemoveQuestion = findViewById(R.id.btnRemoveQuestion);
        rvQuestions = findViewById(R.id.rvQuestions);
        db = FirebaseFirestore.getInstance();
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList);

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(questionAdapter);

        btnAddQuestion.setOnClickListener(v -> showAddQuestionDialog());
        btnRemoveQuestion.setOnClickListener(v -> showRemoveQuestionDialog());
        fetchQuestions();
    }

    private void showAddQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Question");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etQuestion = new EditText(this);
        etQuestion.setHint("Question");
        layout.addView(etQuestion);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String questionText = etQuestion.getText().toString().trim();
            if (!TextUtils.isEmpty(questionText)) {
                Question question = new Question(questionText, questionNumber++);
                String email = LectureLoginActivity.lecsigninemail;
                addQuestion(question, email);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showRemoveQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Question");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etQuestionNumber = new EditText(this);
        etQuestionNumber.setHint("Question Number");
        layout.addView(etQuestionNumber);

        builder.setView(layout);

        builder.setPositiveButton("Remove", (dialog, which) -> {
            String questionNumberText = etQuestionNumber.getText().toString().trim();
            if (!TextUtils.isEmpty(questionNumberText)) {
                int questionNumber = Integer.parseInt(questionNumberText);
                String email = LectureLoginActivity.lecsigninemail;
                removeQuestion(questionNumber, email);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void addQuestion(Question question, String email) {
        db.collection("questions").document(email).collection("questions_list").add(question)
                .addOnSuccessListener(aVoid -> {
                    questionList.add(question);
                    questionAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add question", Toast.LENGTH_SHORT).show());
    }

    private void removeQuestion(int questionNumber, String email) {
        db.collection("questions").document(email).collection("questions_list")
                .whereEqualTo("questionNumber", questionNumber)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection("questions").document(email).collection("questions_list")
                                    .document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        questionList.removeIf(q -> q.getQuestionNumber() == questionNumber);
                                        questionAdapter.notifyDataSetChanged();
                                        Toast.makeText(this, "Question removed successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove question", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(this, "Question not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show());
    }

    private void fetchQuestions() {
        String email = LectureLoginActivity.lecsigninemail;
        db.collection("questions").document(email).collection("questions_list")
                .orderBy("questionNumber") // Ensure the questions are sorted by questionNumber
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    questionList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Question question = document.toObject(Question.class);
                        questionList.add(question);
                    }
                    questionAdapter.notifyDataSetChanged();
                    // Update the question number to be the next available number
                    if (!questionList.isEmpty()) {
                        questionNumber = questionList.get(questionList.size() - 1).getQuestionNumber() + 1;
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show());
    }
}
