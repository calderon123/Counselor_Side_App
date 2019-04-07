package com.example.counselor_side_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.counselor_side_app.R;
import com.example.counselor_side_app.models.Mentees;
import com.example.counselor_side_app.models.UserMentee;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class MenteeAdapter extends RecyclerView.Adapter<MenteeAdapter.ViewHolder> {

    private UserMentee userMentee;
    private Context mContext;
    private List<Mentees> mUsers;
    private boolean ischat;



    public MenteeAdapter(Context mcontext, List<Mentees> mUsers){
        this.mContext = mcontext;
        this.mUsers = mUsers;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.mentorlist500_1000_item, parent, false);
        return new MenteeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Mentees mentees= mUsers.get(i);
        viewHolder.email.setText(mentees.getEmail());

//        if (userMentor.getImage().equals("default")){
        viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
//        }else{
//            Glide.with(mContext).load(userMentor.getImage()).into(viewHolder.profile_image);
//        }
//        if (ischat){
//            if (userMentor.getStatus().equals("online")){
//                viewHolder.img_on.setVisibility(View.VISIBLE);
//                viewHolder.img_off.setVisibility(View.GONE);
//            }else {
//                viewHolder.img_on.setVisibility(View.GONE);
//                viewHolder.img_off.setVisibility(View.VISIBLE);
//            }
//        }else {
//            viewHolder.img_on.setVisibility(View.GONE);
//            viewHolder.img_off.setVisibility(View.GONE);
//        }

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(mContext, MessageActivity.class);
//                intent.putExtra("id", counselors.getId());
//                mContext.startActivity(intent );
//            }
//        });
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

            profile_image = itemView.findViewById(R.id.profile_image);
            fullname = itemView.findViewById(R.id.fullname);
            email = itemView.findViewById(R.id.email);
            img_off = itemView.findViewById(R.id.img_off);
            img_on= itemView.findViewById(R.id.img_on);
        }

    }

}
