package com.example.counselor_side_app.adapters;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.counselor_side_app.R;
import com.example.counselor_side_app.activities.MessageActivity;
import com.example.counselor_side_app.models.Schedules;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter <ScheduleAdapter.ScheduleViewHolder> {

    private Dialog dialog;
    private Context mContext;
    private List<Schedules> schedulesList;
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    public ScheduleAdapter(Context mContext,List<Schedules> schedulesList){
        this.schedulesList = schedulesList;
        this.mContext = mContext;
    }

    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.appointment_schedules, viewGroup, false );
        return new ScheduleAdapter.ScheduleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder scheduleViewHolder, int i) {
        final Schedules schedules = schedulesList.get(i);

        scheduleViewHolder.date.setText("  Date: " + schedules.getDate_sched());
        scheduleViewHolder.where.setText(schedules.getSet_dscrpt());
        scheduleViewHolder.date_schedule.setText(schedules.getDate_sched());
        scheduleViewHolder.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.line1.setVisibility(View.GONE);
            }
        });

        scheduleViewHolder.view_schedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.line1.setVisibility(View.VISIBLE);
                scheduleViewHolder.btn_view_sched.setVisibility(View.VISIBLE);
            }
        });
        scheduleViewHolder.btn_view_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.title1.setVisibility(View.VISIBLE);
            }
        });

        scheduleViewHolder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.title1.setVisibility(View.GONE);
            }
        });
        scheduleViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.edit_part.setVisibility(View.VISIBLE);
                scheduleViewHolder.title1.setVisibility(View.GONE);
                scheduleViewHolder.btn_view_sched.setVisibility(View.GONE);
            }
        });
        scheduleViewHolder.btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                int day  = calendar.get(Calendar.DAY_OF_MONTH -1);
                int month  = calendar.get(Calendar.MONTH -1);
                int year  = calendar.get(Calendar.YEAR -1);

               DatePickerDialog datePickerDialog = new DatePickerDialog(
                        mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year , int month, int dayOfMonth) {
                                Intent intent  = new Intent(mContext, Notification.class);

                                String date = dayOfMonth+ "/"+month +"/"+year ;
                                scheduleViewHolder.date_schedule_edit.setText(date);
                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        scheduleViewHolder.date_schedule_edit.setText(schedules.getDate_sched());
        scheduleViewHolder.where_edit.setText(schedules.getSet_dscrpt());
        scheduleViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.edit_part.setVisibility(View.GONE);
                scheduleViewHolder.btn_view_sched.setVisibility(View.VISIBLE);
            }
        });

        scheduleViewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schedules").
                child(firebaseUser.getUid()).child(schedules.getMentee_sched_id())
                        .child(firebaseUser.getUid());


                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("date_sched", scheduleViewHolder.date_schedule_edit.getText().toString());
                hashMap.put("set_dscrpt", scheduleViewHolder.where_edit.getText().toString());


                databaseReference.updateChildren(hashMap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedulesList.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{

        Button btn_calendar;
        TextView date,btn_view_sched,date_schedule,where,close,cancel,save;
        ImageButton hide,view_schedules,edit;
        EditText date_schedule_edit,where_edit;
        LinearLayout line1,title1,edit_part;

        public ScheduleViewHolder(View itemView){
            super(itemView);

            btn_calendar = itemView.findViewById(R.id.btn_calendar);
            edit_part = itemView.findViewById(R.id.edit_part);
            date_schedule_edit = itemView.findViewById(R.id.date_schedule_edit);
            where_edit = itemView.findViewById(R.id.where_edit);
            edit = itemView.findViewById(R.id.edit);
            date_schedule = itemView.findViewById(R.id.date_schedule);
            where = itemView.findViewById(R.id.where);
            close = itemView.findViewById(R.id.close);
            title1 = itemView.findViewById(R.id.title1);
            view_schedules = itemView.findViewById(R.id.view_schedules);
            btn_view_sched = itemView.findViewById(R.id.btn_view_sched);
            hide = itemView.findViewById(R.id.hide);
            date = itemView.findViewById(R.id.date);
            line1 = itemView.findViewById(R.id.line1);
            cancel = itemView.findViewById(R.id.cancel);
            save = itemView.findViewById(R.id.save);
        }
    }
}
