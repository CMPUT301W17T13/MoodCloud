package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/** The activity for adding a {@link Post} or editing an existing one. */
public class AddOrEditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_post);

        final Post post;
        final PostController postController = new PostController();

        final EditText textExplanation = (EditText) findViewById(R.id.body);
        final EditText testTrigger = (EditText) findViewById(R.id.trigger);
        Button buttonAngry = (Button) findViewById(R.id.angry_selected);
        Button buttonConfused = (Button) findViewById(R.id.confused_selected);
        Button buttonDisgusted = (Button) findViewById(R.id.disgusted_selected);
        Button buttonScared = (Button) findViewById(R.id.scared_selected);
        Button buttonHappy = (Button) findViewById(R.id.happy_selected);
        Button buttonSad = (Button) findViewById(R.id.sad_selected);
        Button buttonAshamed = (Button) findViewById(R.id.ashamed_selected);
        Button buttonSuprised = (Button) findViewById(R.id.surprised_selected);

        


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
