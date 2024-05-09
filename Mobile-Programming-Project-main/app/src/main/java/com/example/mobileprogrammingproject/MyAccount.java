package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MyAccount extends BaseActivity{

    DatabaseReference reference;
    DatabaseReference emailReference;
    FirebaseAuth auth;
    FirebaseUser user;

    EditText email;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText address;
    Button saveButton;
    Toast toast;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        // Firebase authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("data");
        emailReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("data").child("email");


        // Grab the edit text fields
        firstName = findViewById(R.id.firstName_details);
        lastName = findViewById(R.id.lastName_details);
        phoneNumber = findViewById(R.id.phoneNumber_details);
        address = findViewById(R.id.address_details);
        saveButton = findViewById(R.id.button_details);

        reference.child("user_details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, String> userDetails = (Map<String, String>) dataSnapshot.getValue();
                    if (userDetails != null) {
                        firstName.setText(userDetails.get("firstName"));
                        lastName.setText(userDetails.get("lastName"));
                        phoneNumber.setText(userDetails.get("phoneNumber"));
                        address.setText(userDetails.get("address"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("MyAccount", "Failed to read value.", databaseError.toException());
            }
        });

        emailReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String emailText = dataSnapshot.getValue(String.class);
                    email.setText(emailText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("MyAccount", "Failed to read value.", databaseError.toException());
            }
        });

        // Grab the edit text fields
        email = findViewById(R.id.email_details);
        firstName = findViewById(R.id.firstName_details);
        lastName = findViewById(R.id.lastName_details);
        phoneNumber = findViewById(R.id.phoneNumber_details);
        address = findViewById(R.id.address_details);
        saveButton = findViewById(R.id.button_details);

        saveButton.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String firstNameText = firstName.getText().toString();
            String lastNameText = lastName.getText().toString();
            String phoneNumberText = phoneNumber.getText().toString();
            String addressText = address.getText().toString();

            saveUserDetails(reference, emailText, firstNameText, lastNameText, phoneNumberText, addressText);
            toast = Toast.makeText(getApplicationContext(), "Profile Saved!", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void saveUserDetails(DatabaseReference reference, String email, String firstName, String lastName, String phoneNumber, String address) {

        // hashmap to store the user's details
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("email", email);
        userDetails.put("firstName", firstName);
        userDetails.put("lastName", lastName);
        userDetails.put("phoneNumber", phoneNumber);
        userDetails.put("address", address);

        // Save the user's details to the database
        reference.child("user_details").setValue(userDetails);
    }
}




