package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
//import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//mwschafe commented out unused import statements
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

//import static com.csahmad.moodcloud.R.id.angry_selected;

/** The activity for adding a {@link Post} or editing an existing one. */
public class AddOrEditPostActivity extends AppCompatActivity {
    private static final int TAKE_IMAGE_REQUEST = 0;
    private String image;
    private ImageView moodPhoto;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){

        if (requestCode == TAKE_IMAGE_REQUEST){

            if (resultCode == RESULT_OK){
                this.image = intent.getStringExtra("IMAGE");
                Bitmap bitmap = intent.getParcelableExtra("BITMAP");
                this.moodPhoto.setImageBitmap(bitmap);
            }
            else if (resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Photo was cancelled!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Unknown bug! Please report!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_post);
        PostController postController = new PostController();
        moodPhoto = (ImageView)findViewById(R.id.moodPhoto);

        if(isNetworkAvailable()) {
            Intent intent = getIntent();
            String id = intent.getStringExtra("POST_ID");



            try {

                final EditText textExplanation = (EditText) findViewById(R.id.body);
                final EditText textTrigger = (EditText) findViewById(R.id.trigger);

                final RadioGroup moodButtons = (RadioGroup) findViewById(R.id.moodRadioGroup);
                final RadioGroup statusButtons = (RadioGroup) findViewById(R.id.statusRadioGroup);



                if (id == null) {
                    ImageButton photoButton = (ImageButton) findViewById(R.id.takephoto);
                    photoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, TakePhotoActivity.class);
                            startActivityForResult(intent,TAKE_IMAGE_REQUEST);
                        }
                    });
                    Button postButton = (Button) findViewById(R.id.postButton);
                    postButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (textExplanation.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Want to say something?", Toast.LENGTH_LONG).show();
                            } else if (onRadioButtonClicked(moodButtons) == 8) {
                                Toast.makeText(getApplicationContext(), "Want to select a mood?", Toast.LENGTH_LONG).show();
                            } else if (textTrigger.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Want to say why?", Toast.LENGTH_LONG).show();
                            } else if (onStatusButtonClicked(statusButtons) == 4) {
                                Toast.makeText(getApplicationContext(), "Want to select a social context?", Toast.LENGTH_LONG).show();
                            } else {
                                Profile profile = LocalData.getSignedInProfile(getApplicationContext());
                                Post post = new Post(textExplanation.getText().toString(), onRadioButtonClicked(moodButtons),
                                        textTrigger.getText().toString(), image, onStatusButtonClicked(statusButtons),
                                        profile.getId(), null, Calendar.getInstance());
                                PostController postController = new PostController();
                                postController.addOrUpdatePosts(post);
                                ProfileController profileController = new ProfileController();
                                profileController.addOrUpdateProfiles(profile);


                                finish();
                            }
                        }

                    });
                } else {
                    ImageButton photoButton = (ImageButton) findViewById(R.id.takephoto);
                    photoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, TakePhotoActivity.class);
                            startActivityForResult(intent,TAKE_IMAGE_REQUEST);
                        }
                    });

                    final Post oldPost = postController.getPostFromId(id);
                    this.moodPhoto.setImageBitmap(ImageConverter.toBitmap(oldPost.getTriggerImage()));
                    String oldExplannation = oldPost.getText();
                    String oldTrigger = oldPost.getTriggerText();
                    int oldmood = oldPost.getMood();
                    int oldcontext = oldPost.getContext();

                    textTrigger.setText(oldTrigger);
                    textExplanation.setText(oldExplannation);
                    moodButtons.check(RadioConverter.getMoodButtonId(oldmood));
                    statusButtons.check(RadioConverter.getContextButtonId(oldcontext));

                    Button postButton = (Button) findViewById(R.id.postButton);
                    postButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (textExplanation.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Want to say something?", Toast.LENGTH_LONG).show();
                            } else if (onRadioButtonClicked(moodButtons) == 8) {
                                Toast.makeText(getApplicationContext(), "Want to select a mood?", Toast.LENGTH_LONG).show();
                            } else if (textTrigger.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Want to say why?", Toast.LENGTH_LONG).show();
                            } else if (onStatusButtonClicked(statusButtons) == 4) {
                                Toast.makeText(getApplicationContext(), "Want to select a social context?", Toast.LENGTH_LONG).show();
                            } else {
                                oldPost.setMood(onRadioButtonClicked(moodButtons));
                                oldPost.setContext(onStatusButtonClicked(statusButtons));
                                oldPost.setText(textExplanation.getText().toString());
                                oldPost.setTriggerText(textTrigger.getText().toString());
                                oldPost.setTriggerImage(image);
                                oldPost.setDate(Calendar.getInstance());

                                PostController postController = new PostController();
                                postController.addOrUpdatePosts(oldPost);


                                //Context context = v.getContext();
                                //Intent intent = new Intent(context, ViewPostActivity.class);

                                //intent.putExtra("POST_ID", oldPost.getId());
                                finish();
                                //startActivity(intent);
                            }


                        }

                    });
                }

            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }//else{

        //}






        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                finish();
            }}
        );


    }
    //Based on https://developer.android.com/guide/topics/ui/controls/radiobutton.html
    //A converter function that converts the selected radio button in a radio group to a mood
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
                //throw new RuntimeException("fsdfdssdfsd");
                return 8;
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
                //throw new RuntimeException("other fsdfdssdfsd");
                return 4;
        }
        return status;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






}
