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
                Profile profile = LocalData.getSignedInProfile();
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
    public String onRadioButtonClicked(View view) {

        boolean checked = ((RadioGroup) view).getCheckedRadioButtonId() != -1;
        String mood = null;

        switch(view.getId()) {
            case R.id.angry_selected:
                if (checked)

                    mood = "Angry";
            case R.id.confused_selected:
                if (checked)
                    mood = "Confused";
            case R.id.scared_selected:
                if (checked)
                    mood = "Scared";

            case R.id.happy_selected:
                if (checked)
                    mood = "Happy";
            case R.id.sad_selected:
                if (checked)
                    mood = "Sad";
            case R.id.surprised_selected:
                if (checked)
                    mood = "Suprised";
            case R.id.ashamed_selected:
                if (checked)
                    mood = "Ashamed";
            case R.id.disgusted_selected:
                if (checked)
                    mood = "Disgusted";
        }
        return mood;
    }

    public String onStatusButtonClicked(View view) {

        boolean checked = ((RadioGroup) view).getCheckedRadioButtonId() != -1;
        String status = null;

        switch (view.getId()) {
            case R.id.alone_selected:
                if (checked)

                    status = "Alone";
            case R.id.crowd_selected:
                if (checked)

                    status = "Crowd";
            case R.id.group_selected:
                if (checked)

                    status = "Group";
        }
        return status;
    }
}
