package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JournalActivity extends  BaseActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    TextView journalTitle;
    TextView journalBody;
    Button deleteButton;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Get the title and body from the MainActivity
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        String journalId = getIntent().getStringExtra("journalId");

        journalTitle = findViewById(R.id.journal_title_text);
        journalBody = findViewById(R.id.journal_body_text);
        deleteButton = findViewById(R.id.delete_btn);
        editButton = findViewById(R.id.edit_btn);

        // Set the title and body to the TextViews
        journalTitle.setText(title);
        journalBody.setText(body);

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddJournal.class);
            intent.putExtra("title", title);
            intent.putExtra("body", body);
            intent.putExtra("journalId", journalId);
            startActivity(intent);
        });


        // The delete button will delete the journal entry from the database and on success, take the user back to the MainActivity
        deleteButton.setOnClickListener(v -> {
            deleteJournalEntry(journalId);
            finish();
        });
    }


    private void deleteJournalEntry(String journalId) {
        // Get the journal entry from the database and remove it
        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("data").child("journal");
        reference.child(journalId).removeValue();
    }

}
