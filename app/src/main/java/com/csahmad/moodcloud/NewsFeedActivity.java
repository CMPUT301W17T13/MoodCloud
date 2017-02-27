package com.csahmad.moodcloud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/** The activity for viewing the latest mood events from people the signed in user follows. */
public class NewsFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
    }
}