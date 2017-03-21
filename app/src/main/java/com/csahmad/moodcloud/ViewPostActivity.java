package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by Taylor on 2017-03-21.
 */

public class ViewPostActivity extends AppCompatActivity {

    PostController postController = new PostController();
    ProfileController profileController = new ProfileController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Intent intent = getIntent();
        String id = intent.getStringExtra("POST_ID");
        try {
            Post post = postController.getPostFromId(id);
            TextView nameText = (TextView) findViewById(R.id.nameText);
            nameText.setText("Name: " + profileController.getProfileFromID(post.getPosterId()).getName());
            TextView textText = (TextView) findViewById(R.id.textText);
            textText.setText(post.getText());
            TextView dateText = (TextView) findViewById(R.id.dateText);
            dateText.setText(post.getDate().toString());
            TextView contextText = (TextView) findViewById(R.id.contextText);
            ArrayList<String> contexts = new ArrayList<>();
            contexts.add("Alone");
            contexts.add("With a Group");
            contexts.add("In a Crowd");
            contextText.setText(contexts.get(post.getContext()));
            TextView triggerText = (TextView) findViewById(R.id.triggerText);
            triggerText.setText("Trigger: " + post.getTriggerText());
        } catch (TimeoutException e){}

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, NewsFeedActivity.class);
                startActivity(intent);
            }}
        );
    }
}
