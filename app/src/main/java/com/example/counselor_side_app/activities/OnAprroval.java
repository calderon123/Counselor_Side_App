package com.example.counselor_side_app.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.counselor_side_app.R;
import com.example.counselor_side_app.models.UserMentor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OnAprroval extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private Query query;
    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_aprroval);

        logout = findViewById(R.id.logout);
        firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        query = FirebaseDatabase.getInstance().getReference("UserMentor")
        .child(firebaseUser.getUid());

//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserMentor userMentor = dataSnapshot.getValue(UserMentor.class);
//                if (userMentor.getIsApproved().equals("true")){
//                    startActivity(new Intent(OnAprroval.this, MenteeMainActivity.class));
//                    finish();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(OnAprroval.this, StartActivity.class));
                finish();
            }
        });


    }

}
