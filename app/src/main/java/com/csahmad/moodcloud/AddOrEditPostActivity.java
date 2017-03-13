package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.csahmad.moodcloud.R.id.angry_selected;

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

        final RadioGroup moodButtons = (RadioGroup) findViewById(R.id.moodRadioGroup);
        final RadioGroup statusButtons = (RadioGroup) findViewById(R.id.statusRadioGroup);


        //public void onRadioButtonClicked(View view) {
            // Is the button now checked?
            //boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            //switch(view.getId()) {
             //   case R.id.angry_selected:
              //      if (checked)

                  //      break;
               //// case R.id.confused_selected:
                 //
                    //    break;
           // }
        //}



        //Button postButton = (Button) findViewById(R.id.postButton);
        //postButton.OnClickListener(new View.OnClickListener(){
            //@Override
            //public void onClick(View v) {

           // }

       // });



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
