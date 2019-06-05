package com.example.twitty.activity;

import android.os.Bundle;

import com.example.twitty.R;
import com.example.twitty.task.TimelineTask;

public class MainActivity extends StandardTimelineActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Home");
    }

    @Override
    void initTask(){
        task = new TimelineTask(tweetAdapter, MainActivity.this);
    }
}