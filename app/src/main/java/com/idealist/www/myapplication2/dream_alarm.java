package com.idealist.www.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class dream_alarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_alarm);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("꿈 알람");
    }
}
