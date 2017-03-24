package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
//import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//mwschafe commented out unused import statements
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Calendar;

//import static com.csahmad.moodcloud.R.id.angry_selected;

/** The activity for adding a {@link Post} or editing an existing one. */
public class AddOrEditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_post);
        PostController postController = new PostController();


        //Post oldPost = postController.getPostFromId(post.get);
        //String oldExplannation = oldPost.getText();
        //String oldTrigger = oldPost.getTriggerText();
        //

        final EditText textExplanation = (EditText) findViewById(R.id.body);
        final EditText textTrigger = (EditText) findViewById(R.id.trigger);

        final RadioGroup moodButtons = (RadioGroup) findViewById(R.id.moodRadioGroup);
        final RadioGroup statusButtons = (RadioGroup) findViewById(R.id.statusRadioGroup);

        //if (id == null) {
            //setTitle("New Post");
        //} else{
            //setTile("Edit Post");



        //}
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
    public Integer onRadioButtonClicked(View view) {

        int checked = ((RadioGroup) view).getCheckedRadioButtonId();
        Integer mood = null;

        Log.i("selected", Integer.toString(view.getId()));
        Log.i("selected", Integer.toString(R.id.confused_selected));

        switch(checked) {

            case R.id.angry_selected:
                mood = Mood.ANGRY;
                break;

            case R.id.confused_selected:
                mood = Mood.CONFUSED;
                break;

            case R.id.scared_selected:
                mood = Mood.SCARED;
                break;

            case R.id.happy_selected:
                mood = Mood.HAPPY;
                break;

            case R.id.sad_selected:
                mood = Mood.SAD;
                break;

            case R.id.surprised_selected:
                mood = Mood.SURPRISED;
                break;

            case R.id.ashamed_selected:
                mood = Mood.ASHAMED;
                break;

            case R.id.disgusted_selected:
                mood = Mood.DISGUSTED;
                break;

            default:
                throw new RuntimeException("fsdfdssdfsd");
        }

        return mood;
    }

    public Integer onStatusButtonClicked(View view) {

        int checked = ((RadioGroup) view).getCheckedRadioButtonId();
        Integer status = null;

        switch (checked) {
            case R.id.alone_selected:
                status = SocialContext.ALONE;
                break;
            case R.id.crowd_selected:
                status = SocialContext.WITH_CROWD;
                break;
            case R.id.group_selected:
                status = SocialContext.WITH_GROUP;
                break;
            default:
                throw new RuntimeException("other fsdfdssdfsd");
        }
        return status;
    }

    //public
    //covert the mood and status back to button selection.
}
