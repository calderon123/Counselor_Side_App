package com.example.counselor_side_app.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.counselor_side_app.Notification.Client;
import com.example.counselor_side_app.Notification.Data;
import com.example.counselor_side_app.Notification.MyResponse;
import com.example.counselor_side_app.Notification.Sender;
import com.example.counselor_side_app.Notification.Token;
import com.example.counselor_side_app.R;
import com.example.counselor_side_app.adapters.MessageAdapter;
import com.example.counselor_side_app.adapters.ScheduleAdapter;
import com.example.counselor_side_app.fragments.APIService;
import com.example.counselor_side_app.models.Chat;
import com.example.counselor_side_app.models.Counselors;
import com.example.counselor_side_app.models.Mentees;
import com.example.counselor_side_app.models.Schedules;
import com.example.counselor_side_app.models.UserMentee;
import com.example.counselor_side_app.models.UserMentor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView fullname, email,date_schedule,btn_view_sched,date;
    Button rate,btn_calendar;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private CalendarView calendarView;
    LinearLayout line1,line2;

    Button btn_set_sched;
    EditText text_send;
    ImageButton btn_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    ScheduleAdapter scheduleAdapter;
    List<Schedules> mSchedules;
    private Toolbar toolbar;
    APIService apiService;
    boolean notify = false;
    EditText set_dscrpt;
    AlertDialog dialog;
    RecyclerView recyclerView,recyclerView1;
    ValueEventListener seenListener;
    Intent intent;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        btn_view_sched = findViewById(R.id.btn_view_sched);
        profile_image = findViewById(R.id.profile_image);
        fullname = findViewById(R.id.fullname);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        final FloatingActionButton schedule = findViewById(R.id.schedule);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String userid = getIntent().getStringExtra("id");


        mSchedules = new ArrayList<>();
        DatabaseReference adding_schedules =   FirebaseDatabase.getInstance().getReference("Schedules")
                .child(firebaseUser.getUid()).child(userid);
        adding_schedules.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSchedules.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Schedules schedules = snapshot.getValue(Schedules.class);
                if (!(mSchedules ==null) && schedules.getCounselor_sched_id().equals(firebaseUser.getUid())
                 && schedules.getMentee_sched_id().equals(userid)) {
                    schedule.setVisibility(View.GONE);
                    mSchedules.add(schedules);
                  }else {
                    schedule.setVisibility(View.VISIBLE);
                }
                }
                scheduleAdapter = new ScheduleAdapter(MessageActivity.this,mSchedules);
                recyclerView1.setAdapter(scheduleAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);

                View view = getLayoutInflater().inflate(R.layout.schedule_btn, null);
                final String userid = getIntent().getStringExtra("id");
                btn_calendar = view.findViewById(R.id.btn_calendar);
                date_schedule = view.findViewById(R.id.date_schedule);
                set_dscrpt = view.findViewById(R.id.set_dscrpt);
                btn_set_sched = view.findViewById(R.id.btn_set_sched);
                TextView close = view.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_calendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();

                        int day  = calendar.get(Calendar.DAY_OF_MONTH -1);
                        int month  = calendar.get(Calendar.MONTH -1);
                        int year  = calendar.get(Calendar.YEAR -1);

                       datePickerDialog = new DatePickerDialog(
                               MessageActivity.this,
                               new DatePickerDialog.OnDateSetListener() {
                                   @Override
                                   public void onDateSet(DatePicker view, int year , int month, int dayOfMonth) {


                                       String date = dayOfMonth+ "/"+month +"/"+year ;

                                       date_schedule.setText(date);

                                   }
                               }, year, month, day);

                       datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });

                btn_set_sched.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date_schedule_ = date_schedule.getText().toString();
                        String set_dscrpt_ = set_dscrpt.getText().toString();
                        String msg = date_schedule_ +"\n"+ set_dscrpt_;

                        if (!msg.equals("") && !set_dscrpt_.equals("") && !date_schedule_.equals("")) {
                            date_schedule.setText("");
                            set_dscrpt.setText("");
                            setSched(date_schedule_,set_dscrpt_);



                            sendMessage(firebaseUser.getUid(), userid, msg);
                        } else {
                            Toast.makeText(MessageActivity.this,"Can't set empty fields", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                builder.setView(view);
                dialog = builder.create();
                dialog.show();

            }
        });




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();


                if (!msg.equals("")){
                    sendMessage(firebaseUser.getUid(), userid ,msg);
                }else{
                    Toast.makeText(MessageActivity.this, "You cant send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });




        databaseReference = FirebaseDatabase.getInstance().getReference("Add").child(firebaseUser.getUid())
                .child("mentees").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mentees mentees = dataSnapshot.getValue(Mentees.class);

                FirebaseDatabase.getInstance().getReference("UserMentee").child(mentees.getId())

                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getChildren();

                                UserMentee user = dataSnapshot.getValue(UserMentee.class);
                                fullname.setText(user.getFullname());
                                if (profile_image.equals("default")){
                                    profile_image.setImageResource(R.mipmap.ic_launcher);
                                }else {
                                    Picasso.get().load(user.getImageURL()).into(profile_image);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                readMessage(firebaseUser.getUid(),userid ,mentees.getImageurl() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userid);
    }


    private void setSched(String date_schedule_, String set_dscrpt_) {
        final String userid = getIntent().getStringExtra("id");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mentee_sched_id", userid);
        hashMap.put("counselor_sched_id", firebaseUser.getUid());
        hashMap.put("set_dscrpt", set_dscrpt_);
        hashMap.put("date_sched", date_schedule_);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Schedules");
        databaseReference1.child(firebaseUser.getUid()).child(userid).child(firebaseUser.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Calendar calendar  = Calendar.getInstance();

                        calendar.set(Calendar.HOUR_OF_DAY, 11);
                        calendar.set(Calendar.MINUTE, 00);
                        calendar.set(Calendar.SECOND, 30);

                        Intent intent  = new Intent(getApplicationContext(),Notification.class);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        AlarmManager alarmManager  = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);



                        Toast.makeText(MessageActivity.this, "Meeting schedule setted succesfully" , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });




    }

    private void seenMessage(final String userid){
        databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        seenListener= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String delegate() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    private  void sendMessage(String sender, final String receiver, String message){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        databaseReference.child("Chats").push().setValue(hashMap);
        final String userid = getIntent().getStringExtra("id");
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRef.child("id").setValue(userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = message;

        databaseReference = FirebaseDatabase.getInstance().getReference("Add").child(userid)
                .child("counselor").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Counselors counselors= dataSnapshot.getValue(Counselors.class);

                FirebaseDatabase.getInstance().getReference("UserMentor").child(counselors.getId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserMentor userMentor = dataSnapshot.getValue(UserMentor.class);
                                if (notify) {
                                    sendNotification(receiver, userMentor.getFullname(), msg);
                                }
                                notify = false;

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendNotification(String receiver, final String fullname, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        final String userid = getIntent().getStringExtra("id");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, fullname+": "+message,
                            "New Message",userid);

                    Sender sender = new Sender(data ,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200 ){
                                        if (response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readMessage(final String myid, final String userid, final String imageUrl){
        mChat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();

        editor.putString("currentuser",userid);
        editor.apply();

    }

    private void status(String status){

        databaseReference  = FirebaseDatabase.getInstance().getReference("UserMentor").
                child(firebaseUser.getUid());

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);

        databaseReference.updateChildren(hashMap);

    }
    @Override
    protected void onResume(){
        final String userid = getIntent().getStringExtra("id");
        super.onResume();
        status("online");
        currentUser(userid);
    }
    @Override
    protected  void onPause(){
        super.onPause();
        databaseReference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }


}