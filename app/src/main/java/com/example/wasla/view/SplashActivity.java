package com.example.wasla.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mahmoudabdelfatahabd on 08-Apr-18.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemClock.sleep(2*1000);
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
        finish();
    }
}

