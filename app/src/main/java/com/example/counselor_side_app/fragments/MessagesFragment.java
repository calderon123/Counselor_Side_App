package com.example.counselor_side_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.counselor_side_app.Notification.Token;
import com.example.counselor_side_app.R;
import com.example.counselor_side_app.adapters.MenteeAdapter;
import com.example.counselor_side_app.models.ChatList;
import com.example.counselor_side_app.models.Mentees;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class MessagesFragment extends Fragment {


    private RecyclerView recyclerView;

    private MenteeAdapter menteeAdapter;
    private List<ChatList> mUserslist;


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<Mentees> mUsers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_messages, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserslist = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Chats");


        reference  = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserslist.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren() ){
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    mUserslist.add(chatList);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return  view;
    }
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }
    private void chatList() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Add")
                .child(firebaseUser.getUid())
                .child("mentees");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Mentees counselors = snapshot.getValue(Mentees.class);
                    for (ChatList chatList :mUserslist){
                        if(counselors.getId().equals(chatList.getId())) {
                            mUsers.add(counselors);
                        }
                    }
                }

                menteeAdapter = new MenteeAdapter(getContext(), mUsers ,true);
                recyclerView.setAdapter(menteeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
