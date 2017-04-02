package com.csahmad.moodcloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/** The main activity.
 *
 * called upon launch to determine which activity to go to first:
 * {@link NewsFeedActivity} if a user is already signed in,
 * {@link SignInActivity} if no user is signed in
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (LocalData.getSignedInProfile(getApplicationContext()) == null) {

            Intent intent = new Intent(this, SignInActivity.class);
            this.startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, NewsFeedActivity.class);
            this.startActivity(intent);
        }
    }
}

