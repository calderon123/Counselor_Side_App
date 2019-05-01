package com.example.counselor_side_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.counselor_side_app.R;
import com.example.counselor_side_app.activities.MessageActivity;
import com.example.counselor_side_app.models.Mentees;
import com.example.counselor_side_app.models.UserMentee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenteeListAdapter extends RecyclerView.Adapter<MenteeListAdapter.ViewHolder> {


    private Context mContext;
    private List<Mentees> mUsers;
    private boolean ischat;
    String theLastMessage;


    public MenteeListAdapter(Context mcontext, List<Mentees> userMentorList, boolean ischat){
        this.mContext = mcontext;
        this.mUsers = userMentorList;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.mentee_list_adapter, parent, false);
        return new MenteeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Mentees mentees = mUsers.get(i);

        FirebaseDatabase.getInstance().getReference("UserMentee")
                .child(mentees.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMentee userMentee = dataSnapshot.getValue(UserMentee.class);
                viewHolder.fullname.setText(userMentee.getFullname());

                viewHolder.email.setText(userMentee.getEmail());

                if (userMentee.getImageURL().equals("default")){
                    viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(mContext).load(userMentee.getImageURL()).into(viewHolder.profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("UserMentee").child(mentees.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getChildren();

                        UserMentee user = dataSnapshot.getValue(UserMentee.class);
                        if (mentees.getId().equals(user.getId())) {
                            viewHolder.status(user.getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("id", mentees.getId());
                mContext.startActivity(intent );
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView fullname,email;
        public CircleImageView profile_image;
        private CircleImageView img_off;
        private CircleImageView img_on;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.fullname);
            email = itemView.findViewById(R.id.email);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_off = itemView.findViewById(R.id.img_off);
            img_on= itemView.findViewById(R.id.img_on);

        }
        private void status(String id){

            if (ischat){
                if (id.equals("online")){
                    img_on.setVisibility(View.VISIBLE);
                    img_off.setVisibility(View.GONE);
                }else {
                    img_on.setVisibility(View.GONE);
                    img_off.setVisibility(View.VISIBLE);
                }
            }else {
                img_on.setVisibility(View.GONE);
                img_off.setVisibility(View.GONE);

            }


        }


    }
}
