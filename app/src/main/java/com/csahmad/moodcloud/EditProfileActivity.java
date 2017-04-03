package com.csahmad.moodcloud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/** The activity for editing a {@link Profile}. */
public class EditProfileActivity extends AppCompatActivity {

    private Profile profile;
    private Account account;

    private EditText name;
    private EditText oldPassword;
    private EditText newPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //a back button
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                finish();
            }}
        );

        this.profile = LocalData.getSignedInProfile(this);
        this.account = LocalData.getSignedInAccount(this);

        this.name = (EditText) this.findViewById(R.id.profileName);
        this.name.setText(profile.getName());

        this.oldPassword = (EditText) this.findViewById(R.id.oldPassword);
        this.newPassword = (EditText) this.findViewById(R.id.newPassword);
    }

    /** Save the new info to elasticsearch and locally then exit this Activity. */
    public void onSaveProfileClick(View v) {

        String nameText = this.name.getText().toString().trim();
        String oldPasswordText = this.oldPassword.getText().toString().trim();
        String newPasswordText = this.newPassword.getText().toString().trim();

        if (!newPasswordText.equals("")) {

            if (oldPasswordText.equals("")) {

                Toast.makeText(getApplicationContext(), "Must enter current password",
                        Toast.LENGTH_LONG).show();

                return;
            }

            if (!oldPasswordText.equals(this.account.getPassword())) {

                Toast.makeText(getApplicationContext(), "Incorrect password",
                        Toast.LENGTH_LONG).show();

                return;
            }

            this.account.setPassword(newPasswordText);
        }

        if (!nameText.equals(""))
            this.profile.setName(nameText);

        AccountController accountController = new AccountController();
        this.account.setProfile(this.profile);
        accountController.addOrUpdateAccounts(this.account);
        LocalData.store(this.account, this);

        ProfileController profileController = new ProfileController();
        profileController.addOrUpdateProfiles(this.profile);


        this.finish();
    }
}
