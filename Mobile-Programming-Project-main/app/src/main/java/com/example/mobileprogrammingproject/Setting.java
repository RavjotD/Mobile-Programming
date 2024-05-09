package com.example.mobileprogrammingproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Setting extends BaseActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //  Add your account related functionality here

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("data").child("email");

        username = findViewById(R.id.userName);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String email = snapshot.getValue(String.class);
                    username.setText(email);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
