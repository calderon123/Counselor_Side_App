package com.example.counselor_side_app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.counselor_side_app.R;

/**
 * Created by AbhiAndroid
 */

public class Access extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Access.this, OnAprroval.class);
                startActivity(intent);
                finish();
            }
        },5000);

    }
}