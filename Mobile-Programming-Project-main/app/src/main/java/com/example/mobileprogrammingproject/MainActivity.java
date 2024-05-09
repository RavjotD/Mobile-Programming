package com.example.mobileprogrammingproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends BaseActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    DatabaseReference journalReference;
    ImageButton addJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // ImageButton to add a journal
        addJournal = findViewById(R.id.add_journal_button);

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        // Get the user's email and save it to the database
        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("data");

        // Get the user's journal entries
        journalReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("data").child("journal");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if user node exists
                if (!dataSnapshot.exists()) {
                    // User node doesn't exist, you can proceed to save the email
                    saveEmailToDatabase(reference, user.getEmail());
                } else {
                    // User node already exists, handle accordingly (optional)
                    System.out.println("User node already exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.e(TAG, "Database Error: " + databaseError.getMessage());
            }
        });

        // Populate the ScrollView with the user's journal entries
        journalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get a reference to the LinearLayout container inside ScrollView
                LinearLayout linearLayout = findViewById(R.id.linear_layout_container);

                // Clear existing views before adding new ones
                linearLayout.removeAllViews();

                Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.holtwood_one_sc);

                // Loop through the journal entries
                for (DataSnapshot journalSnapshot : dataSnapshot.getChildren()) {
                    // Get the journal entry as a Map
                    Map<String, Object> entry = (Map<String, Object>) journalSnapshot.getValue();

                    // Get the title of the journal entry
                    assert entry != null;
                    String title = (String) entry.get("title");
                    String body = (String) entry.get("body");

                    // For each journal entry, create a new CardView
                    CardView cardView = new CardView(MainActivity.this);

                    // Set some properties of the CardView
                    cardView.setRadius(50);  // In pixels
                    cardView.setContentPadding(0, 200, 0, 200);
                    cardView.setClickable(true);
                    cardView.setCardBackgroundColor(Color.parseColor("#32b0e6"));

                    // Create a new TextView to put in the CardView
                    TextView textView = new TextView(MainActivity.this);
                    textView.setLayoutParams(new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT,
                            CardView.LayoutParams.WRAP_CONTENT));
                    textView.setText(title);  // Set the text to the journal entry's title
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(24);
                    textView.setAllCaps(true);
                    textView.setTextColor(Color.parseColor("#e9eef2"));
                    textView.setTypeface(typeface, Typeface.BOLD);


                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("body", body);
                            intent.putExtra("journalId", journalSnapshot.getKey());
                            startActivity(intent);
                        }
                    });

                    // Add the TextView to the CardView
                    cardView.addView(textView);

                    // Create new layout parameters for the CardView
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    // Set margins (left, top, right, bottom)
                    int margin = getResources().getDimensionPixelSize(R.dimen.cardview_margin);
                    layoutParams.setMargins(margin, margin, margin, margin);

                    // Add the CardView to the LinearLayout with the layout parameters
                    linearLayout.addView(cardView, layoutParams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.e(TAG, "Database Error: " + databaseError.getMessage());
            }
        });

        // ImageButton to add a journal
        addJournal.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddJournal.class);
            startActivity(intent);
        });
    }

    // Method to save email to the database
    private void saveEmailToDatabase(DatabaseReference reference, String email) {
        reference.child("email").setValue(email);
    }
}