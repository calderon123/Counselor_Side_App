package com.example.counselor_side_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.counselor_side_app.R;
import com.example.counselor_side_app.adapters.MenteeAdapter;
import com.example.counselor_side_app.models.Mentees;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class MenteeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    private RecyclerView add;
    private MenteeAdapter menteeAdapter;
    private List<Mentees> mUsers;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mentor_list, container, false);

        recyclerView = view.findViewById(R.id.fragment_cont);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();
        return view;


    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Add").child(firebaseUser.getUid()).child("mentees");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Mentees mentees= snapshot.getValue(Mentees.class);

                    mUsers.add(mentees);

                }
                menteeAdapter = new MenteeAdapter(getContext(), mUsers );
                recyclerView.setAdapter(menteeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
