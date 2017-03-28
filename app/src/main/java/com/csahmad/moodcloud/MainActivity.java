package com.csahmad.moodcloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/** The main activity. */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(LocalData.getSignedInProfile((getApplicationContext())));

        Intent intent = new Intent(this, SignInActivity.class);
        this.startActivity(intent);
/*
>>>>>>> origin
        if (LocalData.getSignedInProfile(getApplicationContext()) == null) {

            Intent intent = new Intent(this, SignInActivity.class);
            this.startActivity(intent);
<<<<<<< HEAD
        }

        else {
            Intent intent = new Intent(this, NewsFeedActivity.class);
            this.startActivity(intent);
        }
=======
        }*/
    }
}
