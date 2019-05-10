package com.example.counselor_side_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.counselor_side_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterMenteeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText fullname,email,password,confirmpassword;
    private Button btn_register;
    private ImageView profile_image;
    private LinearLayout line1;
    private  ArrayAdapter<CharSequence> adapter, adapter1 ,adapter2;
    private Spinner expertise,availability,rate;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Uri image_uri;
    static int PreqCode =1;
    static int REQUESCODE = 1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mentee);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        rate = findViewById(R.id.rate);
        availability = findViewById(R.id.availability);
        expertise = findViewById(R.id.expertise);
        profile_image = findViewById(R.id.profile_image);
        line1 = findViewById(R.id.line1);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        confirmpassword = findViewById(R.id.confirmpassword);
        progressBar = findViewById(R.id.progressing);

        auth = FirebaseAuth.getInstance();

       adapter = ArrayAdapter.createFromResource(this, R.array.expertise_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.availability_arrays, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.rate_arrays, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        expertise.setAdapter(adapter);
        expertise.setOnItemSelectedListener(this);

        availability.setAdapter(adapter1);
        availability.setOnItemSelectedListener(this);

        rate.setAdapter(adapter2);
        rate.setOnItemSelectedListener(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);

                String rate_ = rate.getSelectedItem().toString();
                String expertise_ = expertise.getSelectedItem().toString();
                String availability_ = availability.getSelectedItem().toString();

                String txt_fullname = fullname.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmpass = confirmpassword.getText().toString();



                if(TextUtils.isEmpty(txt_fullname) || TextUtils.isEmpty(txt_email) ||
                        TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_confirmpass)){
                    Toast.makeText(RegisterMenteeActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
                }else if (txt_password.length() < 6){
                    Toast.makeText(RegisterMenteeActivity.this, "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                }else if(!txt_password.equals(txt_confirmpass)) {
                    Toast.makeText(RegisterMenteeActivity.this, "Password not matched!", Toast.LENGTH_SHORT).show();
                } else {
                    register(rate_,expertise_,availability_,txt_fullname,txt_email,txt_password);
                }
            }
        });
    }


    private void  register (final String rate,final String expertise,final String availability,
                            final String fullname, final String email, final String password){

        progressBar.setVisibility(View.VISIBLE);
        line1.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            progressBar.setVisibility(View.VISIBLE);
                            line1.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            reference = FirebaseDatabase.getInstance().getReference("UserMentor").child(userid);
                            FirebaseAuth auth = FirebaseAuth.getInstance();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("rate", rate);
                            hashMap.put("expertise", expertise);
                            hashMap.put("availability", availability);
                            hashMap.put("address", "default");
                            hashMap.put("imageURL", "default");
                            hashMap.put("fullname", fullname);
                            hashMap.put("email", email);
                            hashMap.put("status", "offline");
                            hashMap.put("isApproved", "false");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterMenteeActivity.this,
                                                "Registration Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterMenteeActivity.this, MenteeMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else{
                            progressBar.setVisibility(View.GONE);
                            line1.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(RegisterMenteeActivity.this, "You cant register with this email and password" , Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}