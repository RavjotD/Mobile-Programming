package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddJournal extends BaseActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    Button button;
    EditText journalTitle;
    EditText journalBody;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjournal);

        // Firebase authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Get the user's email and save it to the database
        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("data");

        button = findViewById(R.id.submit_journal_entry);
        journalTitle = findViewById(R.id.journal_title);
        journalBody = findViewById(R.id.journal_body);

        // Check if we're in edit mode
        String existingTitle = getIntent().getStringExtra("title");
        String existingBody = getIntent().getStringExtra("body");
        String existingJournalId = getIntent().getStringExtra("journalId");

        if (existingTitle != null && existingBody != null) {
            // We're in edit mode
            journalTitle.setText(existingTitle);
            journalBody.setText(existingBody);
        }

        button.setOnClickListener(v -> {
            String title = journalTitle.getText().toString();
            String body = journalBody.getText().toString();

            if (title.isEmpty() || body.isEmpty()) {
                toast = Toast.makeText(getApplicationContext(), "Title or body cannot be empty!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (existingJournalId != null) {
                    // We're in edit mode, so update the existing journal entry
                    updateJournalEntry(reference, existingJournalId, title, body);
                } else {
                    // We're in add mode, so create a new journal entry
                    saveJournalToDatabase(reference, title, body);
                }
                toast = Toast.makeText(getApplicationContext(), "Journal entry saved!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveJournalToDatabase(DatabaseReference reference, String title, String body) {
        // Generate a unique ID for the new journal entry
        String journalId = reference.child("journal").push().getKey();

        // Map to hold the title and body
        Map<String, String> journalEntry = new HashMap<>();
        journalEntry.put("title", title);
        journalEntry.put("body", body);

        // Save the journal entry to the database under the generated ID
        if (journalId != null) {
            reference.child("journal").child(journalId).setValue(journalEntry);
        }
    }

    private void updateJournalEntry(DatabaseReference reference, String journalId, String title, String body) {
        // Map to hold the title and body
        Map<String, String> journalEntry = new HashMap<>();
        journalEntry.put("title", title);
        journalEntry.put("body", body);

        // Update the journal entry in the database under the existing ID
        reference.child("journal").child(journalId).setValue(journalEntry);
    }
}
