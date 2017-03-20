package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
//import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//mwschafe commented out unused import statements
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Calendar;

//import static com.csahmad.moodcloud.R.id.angry_selected;

/** The activity for adding a {@link Post} or editing an existing one. */
public class AddOrEditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_post);


        final PostController postController = new PostController();

        final EditText textExplanation = (EditText) findViewById(R.id.body);
        final EditText textTrigger = (EditText) findViewById(R.id.trigger);

        final RadioGroup moodButtons = (RadioGroup) findViewById(R.id.moodRadioGroup);
        final RadioGroup statusButtons = (RadioGroup) findViewById(R.id.statusRadioGroup);





        Button postButton = (Button) findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Profile profile = LocalData.getSignedInProfile(getApplicationContext());
                Post post = new Post(textExplanation.getText().toString(),onRadioButtonClicked(moodButtons),
                        textTrigger.getText().toString(),null,onStatusButtonClicked(statusButtons),
                        profile.getId() ,null, Calendar.getInstance());
                PostController postController = new PostController();
                postController.addOrUpdatePosts(post);
                ProfileController profileController = new ProfileController();
                profileController.addOrUpdateProfiles(profile);

                Context context = v.getContext();
                Intent intent = new Intent(context, NewsFeedActivity.class);
                startActivity(intent);
            }

        });



        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(AddOrEditPostActivity.this, NewsFeedActivity.class);
                startActivity(intent);
            }}
        );


    }
    //Based on https://developer.android.com/guide/topics/ui/controls/radiobutton.html
    public Mood onRadioButtonClicked(View view) {

        boolean checked = ((RadioGroup) view).getCheckedRadioButtonId() != -1;
        Mood mood = null;

        switch(view.getId()) {
            case R.id.angry_selected:
                if (checked)

                    mood = Mood.Angry;
            case R.id.confused_selected:
                if (checked)
                    mood = Mood.Confused;
            case R.id.scared_selected:
                if (checked)
                    mood = Mood.Scared;

            case R.id.happy_selected:
                if (checked)
                    mood = Mood.Happy;
            case R.id.sad_selected:
                if (checked)
                    mood = Mood.Sad;
            case R.id.surprised_selected:
                if (checked)
                    mood = Mood.Surprised;
            case R.id.ashamed_selected:
                if (checked)
                    mood = Mood.Ashamed;
            case R.id.disgusted_selected:
                if (checked)
                    mood = Mood.Disgusted;
        }
        return mood;
    }

    public SocialContext onStatusButtonClicked(View view) {

        boolean checked = ((RadioGroup) view).getCheckedRadioButtonId() != -1;
        SocialContext status = null;

        switch (view.getId()) {
            case R.id.alone_selected:
                if (checked)

                    status = SocialContext.Alone;
            case R.id.crowd_selected:
                if (checked)

                    status = SocialContext.WithCrowd;
            case R.id.group_selected:
                if (checked)

                    status = SocialContext.WithGroup;
        }
        return status;
    }
}
