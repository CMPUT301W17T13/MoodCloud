package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeoutException;

/** The activity for signing in. */
public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //instantiate required controllers
        final ProfileController profileController = new ProfileController();
        final AccountController accountController = new AccountController();
        final LocalData localData = new LocalData();

        final EditText usernameText = (EditText) findViewById(R.id.username);
        final EditText passwordText = (EditText) findViewById(R.id.password);


        //when user presses the Sign In button
        Button button = (Button) findViewById(R.id.signIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Account account = accountController.getAccountFromUsername(usernameText.getText().toString());

                    //check if password matches
                    //if password is correct, store profile in LocalData and move to news feed
                    if(account.getPassword().equals(passwordText.getText().toString())){
                        localData.store(account.getProfile().getId());
                        Context context = view.getContext();
                        Intent intent = new Intent(context, NewsFeedActivity.class);
                        startActivity(intent);
                    }

                    //if password is incorrect, clear password field and highlight with red
                    else{
                        passwordText.setBackgroundColor(Color.RED);
                        passwordText.setText("");
                        Toast.makeText(getApplicationContext(), "Incorrect Login Information", Toast.LENGTH_LONG).show();
                    }
                }
                catch (TimeoutException e){
                    System.err.println("TimeoutException: " + e.getMessage());
                }
            }
        });

    }
}
