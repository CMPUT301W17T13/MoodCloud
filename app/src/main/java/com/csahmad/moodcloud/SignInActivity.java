package com.csahmad.moodcloud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/** The activity for signing in. */
public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Get sign in info from user and store pass to LocalData.store()
    }
}
