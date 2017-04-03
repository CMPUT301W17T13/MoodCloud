package com.csahmad.moodcloud;

import android.app.Activity;
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

/**
 * SignInActivity allows the user to sign in, or navigate to the account creation activity
 *
 * has only two functions implemented with buttons: Sign in, and Create Account
 *
 * Sign In button checks the entered username and password textfields using elasticsearch
 * Upon finding a match, corresponding profile is stored in LocalDatare
 * and user is directed to the newsfeed activity
 *
 * Create Account button redirects the user to CreateAccountActivity
 *
 * @see ProfileController
 * @see AccountController
 * @see Account
 */
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


        System.out.println("test");
        //when user presses the Sign In button
        Button button = (Button) findViewById(R.id.signIn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try{

                    String username = usernameText.getText().toString();
                    String password = passwordText.getText().toString();

                    if (username.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Username empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (password.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Password empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Account account = accountController.getAccountFromUsername(usernameText.getText().toString());

                    //check if password matches
                    //if password is correct, store profile in LocalData and move to news feed
                    if(account.getPassword().equals(passwordText)){
                        localData.store(account, getApplicationContext());
                        Context context = view.getContext();
                        Intent intent = new Intent(context, NewsFeedActivity.class);
                        startActivity(intent);
                    }

                    //if password is incorrect, clear password field and highlight with red
                    else{
                        passwordText.setBackgroundColor(Color.RED);
                        passwordText.setText("");
                        Toast.makeText(getApplicationContext(), "Incorrect Login Information",
                                Toast.LENGTH_LONG).show();
                    }
                }

                catch (TimeoutException e){
                    System.err.println("TimeoutException: " + e.getMessage());
                }
            }
        });

        //when user presses the Create Account button
        Button button1 = (Button) findViewById(R.id.createAccount);
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                try{

                    String username = usernameText.getText().toString();
                    String password = passwordText.getText().toString();

                    if (username.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Username empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (password.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Password empty",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Boolean unique = accountController.isUsernameUnique(username);

                    if (unique) {
                        Profile profile = new Profile(username);
                        profileController.addOrUpdateProfiles(profile);

                        try {
                            profileController.waitForTask();
                        }

                        catch (Exception e) {
                            throw new RuntimeException("Crash on adding login profile.");
                        }

                        Account account = new Account(username, password, profile);
                        accountController.addOrUpdateAccounts(account);
                        profile.setHomeProfile(true);
                        LocalData.store(account, getApplicationContext());
                        //probably something to sign in the user
                        Context context = view.getContext();
                        Intent intent = new Intent(context, NewsFeedActivity.class);
                        startActivity(intent);

                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Username already exists",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (TimeoutException e){
                    System.err.println("TimeoutException: " + e.getMessage());
                }
            }
        });

    }
    @Override
    public void onBackPressed(){
        
    }
}
