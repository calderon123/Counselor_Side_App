package com.example.counselor_side_app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.counselor_side_app.R;

/**
 * Created by AbhiAndroid
 */

public class MainActivity extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}