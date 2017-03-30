package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by Taylor on 2017-03-21.
 */

public class ViewPostActivity extends AppCompatActivity {

    PostController postController = new PostController();
    ProfileController profileController = new ProfileController();
   //FollowRequestController followRequestController = new FollowRequestController();

    @Override
    protected void onStart() {

        super.onStart();
        setContentView(R.layout.activity_view_post);
        Intent intent = getIntent();
        String id = intent.getStringExtra("POST_ID");
        try {
            final Post post = postController.getPostFromId(id);
            final Profile profile = profileController.getProfileFromID(post.getPosterId());
            TextView nameText = (TextView) findViewById(R.id.nameText);
            nameText.setText("Name: " + profile.getName());
            final TextView textText = (TextView) findViewById(R.id.textText);
            textText.setText(post.getText());
            TextView dateText = (TextView) findViewById(R.id.dateText);
            SimpleDateFormat format1 = new SimpleDateFormat(StringFormats.dateFormat);
            dateText.setText(format1.format(post.getDate().getTime()));
            TextView contextText = (TextView) findViewById(R.id.contextText);
            String[] contexts = new String[]{"Alone","With a Group","In a Crowd"};
            contextText.setText(contexts[post.getContext()]);

            TextView triggerText = (TextView) findViewById(R.id.triggerText);
            triggerText.setText("Trigger: " + post.getTriggerText());
            int[] draws = new int[]{R.drawable.angry,R.drawable.confused,R.drawable.disgusted,
            R.drawable.embarassed,R.drawable.fear,R.drawable.happy,R.drawable.sad,R.drawable.shame,R.drawable.suprised};
            ImageView moodImage = (ImageView) findViewById(R.id.moodImage);
            final Button button = (Button) findViewById(R.id.button);
            final Button deleteButton = (Button) findViewById(R.id.deleteButton);
            if (LocalData.getSignedInProfile(getApplicationContext()).equals(profile)) {
                //button.setText(LocalData.getSignedInProfile().getId() + " " + post.getPosterId());

                button.setText("Edit Post");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        Context context = view.getContext();
                        Intent intent = new Intent(context, AddOrEditPostActivity.class);
                        intent.putExtra("POST_ID",post.getId());
                        startActivity(intent);
                    }}
                );
                deleteButton.setText("Delete Post");
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        postController.deletePosts(post);
                        finish();
                    }}
                );
            } else {
                deleteButton.setVisibility(View.GONE);

                FollowController followController = new FollowController();
                FollowRequestController followRequestController = new FollowRequestController();
                if (followController.followExists(LocalData.getSignedInProfile(getApplicationContext()),profile)){
                    //Button button = (Button) findViewById(R.id.followeditbutton);
                    button.setText("Followed");
                    button.setClickable(FALSE);
                } else {
                    if (followRequestController.requestExists(LocalData.getSignedInProfile(getApplicationContext()),profile)){
                        //Button button = (Button) findViewById(R.id.followeditbutton);
                        button.setText("Request Sent");
                        button.setClickable(FALSE);
                    }else {
                        //Button button = (Button) findViewById(R.id.followeditbutton);
                        button.setText("Send Follow Request");
                        button.setClickable(TRUE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(getApplicationContext(), "button clicked", Toast.LENGTH_LONG).show();
                                //button.setText("button clicked");
                                FollowRequest followRequest = new FollowRequest(
                                        LocalData.getSignedInProfile(getApplicationContext()), profile);
                                FollowRequestController followRequestController = new FollowRequestController();
                                followRequestController.addOrUpdateFollows(followRequest);
                                //textText.setText(followRequest.getId().toString());
                                try {
                                    followRequestController.waitForTask();
                                    //textText.setText(followRequest.getId().toString());
                                    if (followRequestController.getFollowRequestFromID(followRequest.getId()).equals(followRequest)) {
                                        button.setText("Request Sent");
                                    } else {
                                        button.setText("Request Not Sent");
                                    }
                                    button.setClickable(FALSE);

                                } catch (InterruptedException e) {
                                } catch (TimeoutException e) {
                                } catch (ExecutionException e) {
                                }
                            }}
                        );}}
            }
            moodImage.setImageResource(draws[post.getMood()]);
        } catch (TimeoutException e){}

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }}
        );
    }
}
