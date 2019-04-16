package com.example.counselor_side_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.counselor_side_app.R;
import com.example.counselor_side_app.activities.MessageActivity;
import com.example.counselor_side_app.models.Chat;
import com.example.counselor_side_app.models.Mentees;
import com.example.counselor_side_app.models.UserMentee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class MenteeAdapter extends RecyclerView.Adapter<MenteeAdapter.ViewHolder> {

    private UserMentee userMentee;
    private Context mContext;
    private List<Mentees> mUsers;
    private boolean ischat;
    private String user_status;
    String theLastMessage;

    public MenteeAdapter(Context mcontext, List<Mentees> mUsers,boolean ischat){
        this.mContext = mcontext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.mentorlist500_1000_item, parent, false);
        return new MenteeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Mentees mentees= mUsers.get(i);
        viewHolder.email.setText(mentees.getEmail());

//        if (userMentor.getImage().equals("default")){
        viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
//        }else{
//            Glide.with(mContext).load(userMentor.getImage()).into(viewHolder.profile_image);
//        }
        if (ischat){
            lastMessage(mentees.getId() ,viewHolder.last_message);
        }else {
            viewHolder.last_message.setVisibility(View.GONE);
        }
        FirebaseDatabase.getInstance().getReference("UserMentee").child(mentees.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getChildren();

                        UserMentee user = dataSnapshot.getValue(UserMentee.class);

                        viewHolder.bobo(user.getStatus());
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

        public TextView email,last_message;
        public CircleImageView profile_image;
        private CircleImageView img_off;
        private CircleImageView img_on;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            email = itemView.findViewById(R.id.email);
            last_message = itemView.findViewById(R.id.last_message);
            img_off = itemView.findViewById(R.id.img_off);
            img_on= itemView.findViewById(R.id.img_on);
        }
        private void bobo(String id) {

            if (ischat) {
                if (id.equals("online")) {
                    img_on.setVisibility(View.VISIBLE);
                    img_off.setVisibility(View.GONE);
                } else {
                    img_on.setVisibility(View.GONE);
                    img_off.setVisibility(View.VISIBLE);
                }
            } else {
                img_on.setVisibility(View.GONE);
                img_off.setVisibility(View.GONE);

            }
        }

    }
    private void lastMessage(final String userid, final TextView last_message){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chat.getMessage();
                    }

                }
                switch (theLastMessage){
                    case "default":
                        last_message.setText("NoMessage");
                        break;


                    default:
                        last_message.setText(theLastMessage);
                        break;
                }
                theLastMessage= "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
