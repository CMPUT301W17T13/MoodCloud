package com.csahmad.moodcloud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/** The activity for editing a {@link Profile}. */
public class EditProfileActivity extends AppCompatActivity {

    private Profile profile;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.profile = LocalData.getSignedInProfile(this);
        this.account = LocalData.getSignedInAccount(this);

        EditText name = (EditText) this.findViewById(R.id.profileName);
        name.setText(profile.getName());
    }

    /** Save the new info to elasticsearch and locally then exit this Activity. */
    public void onSaveProfileClick(View v) {

        ProfileController controller = new ProfileController();
        controller.addOrUpdateProfiles(profile);
        LocalData.store(account, this);
        this.finish();
    }
}
