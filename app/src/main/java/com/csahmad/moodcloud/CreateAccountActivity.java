package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//mwschafe commented out unused import statements
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
//import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/** The activity for creating an {@link Account}.
 * @author Taylor
 */
public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        final ProfileController profileController = new ProfileController();
        final AccountController accountController = new AccountController();

        final EditText usernameText = (EditText) findViewById(R.id.username);
        final EditText passwordText = (EditText) findViewById(R.id.password);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, SignInActivity.class);
                startActivity(intent);
            }}
        );

        Button button = (Button) findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Boolean unique = accountController.isUsernameUnique(usernameText.getText().toString());

                if (unique) {
                    Profile profile = new Profile(usernameText.getText().toString());
                    profileController.addOrUpdateProfiles(profile);

                    try {
                        profileController.waitForTask();
                    }

                    catch (Exception e) {
                        throw new RuntimeException("Crash on adding login profile.");
                    }

                    Account account = new Account(usernameText.getText().toString(), passwordText.getText().toString(), profile);
                    accountController.addOrUpdateAccounts(account);
                    profile.setHomeProfile(TRUE);
                    LocalData.store(profile);
                    //probably something to sign in the user
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NewsFeedActivity.class);
                    startActivity(intent);

                }
                if (unique == FALSE){
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                }
                } catch (TimeoutException e){
                    System.err.println("TimeoutException: " + e.getMessage());
                }

            }
        });
    }
}
