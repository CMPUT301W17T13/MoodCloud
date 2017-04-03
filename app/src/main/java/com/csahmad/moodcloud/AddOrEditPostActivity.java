package com.csahmad.moodcloud;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;
import android.location.LocationManager;
import com.google.android.gms.location.LocationRequest;

/** The activity for adding a {@link Post} or editing an existing one. */
public class AddOrEditPostActivity extends AppCompatActivity {

    private static final int TAKE_IMAGE_REQUEST = 0;
    private static final int READ_LOCATION_REQUEST = 1;
    private final static int REQUEST_GET_DATE = 3;
    private String image;
    private Drawable defaultImage;
    private ImageButton moodPhoto;
    private TextView dateString;
    private Calendar date;
    private Bundle setDate;
    private int setDay;
    private int setMonth;
    private int setYear;
    private ImageButton deletePhoto;
    private double[] locationArray = null;



    @Override
    // Methods to get the result from the camera and the date picker.
    //Cases are separated by REQUESTS that are ints
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == TAKE_IMAGE_REQUEST) {

            if (resultCode == RESULT_OK) {

                String cameraImage = intent.getStringExtra("IMAGE");

                if (cameraImage != null) {
                    this.image = cameraImage;
                    Bitmap bitmap = intent.getParcelableExtra("BITMAP");

                    this.moodPhoto.setImageBitmap(bitmap);
                    deletePhoto.setVisibility(View.VISIBLE);
                }

            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Photo was cancelled!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Unknown bug! Please report!", Toast.LENGTH_LONG).show();
        }

        if (requestCode == REQUEST_GET_DATE){
            if(resultCode == RESULT_OK){
                setDate = intent.getBundleExtra("set_date");
                setDay = setDate.getInt("day");
                setMonth = setDate.getInt("month");
                setYear = setDate.getInt("year");
                this.date = DateConverter.toDate(setYear,setMonth,setDay);

                dateString.setText(DateConverter.dateToString(date));

            }

        }
    }

    /** Request permission from the user to access location. */
    public void requestLocationPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                READ_LOCATION_REQUEST);
    }

    @Override
    // requesting location, first by requesting permission to use location
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == READ_LOCATION_REQUEST) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                String provider = lm.getBestProvider(new Criteria(), true);

                if (!lm.isProviderEnabled(provider)) {
                    Toast.makeText(getApplicationContext(), "Provider disabled", Toast.LENGTH_LONG).show();
                    Log.i("LocationStatus", "Provider disabled");
                }

                Location location = lm.getLastKnownLocation(provider);

                if (location == null){

                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    LocationListener locationListener= new LocationListener() {

                        @Override
                        public void onLocationChanged(Location location) {

                            Toast.makeText(getApplicationContext(), "Got location!", Toast.LENGTH_LONG).show();
                            Log.i("LocationStatus", "Got location!");
                            locationArray = new double[]{location.getLatitude(),
                                    location.getLongitude(), location.getAltitude()};
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                            if (status == LocationProvider.OUT_OF_SERVICE) {
                                Toast.makeText(getApplicationContext(), "Out of service", Toast.LENGTH_LONG).show();
                                Log.i("LocationStatus", "Out of service");
                            }

                            else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                                Toast.makeText(getApplicationContext(), "Unavailable", Toast.LENGTH_LONG).show();
                                Log.i("LocationStatus", "Unavailable");
                            }

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                            ;
                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                            Toast.makeText(getApplicationContext(), "Disabled", Toast.LENGTH_LONG).show();
                            Log.i("LocationStatus", "Disabled");
                        }
                    };

                    //lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                    lm.requestLocationUpdates(provider, 2_000l, 100f, locationListener);
                }

                else {
                    Toast.makeText(getApplicationContext(), "Got location!", Toast.LENGTH_LONG).show();
                    Log.i("LocationStatus", "Got location!");
                    locationArray = new double[]{location.getLatitude(),
                            location.getLongitude(), location.getAltitude()};
                }
            }

            else {
                Toast.makeText(getApplicationContext(), "Denied!", Toast.LENGTH_LONG).show();
                Log.i("LocationStatus", "Denied!");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_post);

        moodPhoto = (ImageButton) findViewById(R.id.moodPhoto);
        defaultImage = moodPhoto.getDrawable();
        dateString = (TextView) findViewById(R.id.postDate);
        deletePhoto = (ImageButton) findViewById(R.id.delimage);

        // TODO: 2017-03-31 Find out why Android Studio won't let me put this in a separate method
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(), "Requesting permission", Toast.LENGTH_LONG).show();
            Log.i("LocationStatus", "Requesting permission");
            requestLocationPermission();
        }

        if(isNetworkAvailable()) {
            final Intent intent = getIntent();
            final Post post = intent.getParcelableExtra("POST");


            final EditText textExplanation = (EditText) findViewById(R.id.body);
            final EditText textTrigger = (EditText) findViewById(R.id.trigger);

            final RadioGroup moodButtons = (RadioGroup) findViewById(R.id.moodRadioGroup);
            final RadioGroup statusButtons = (RadioGroup) findViewById(R.id.statusRadioGroup);
            final EditText latitudetext = (EditText) findViewById(R.id.latitude);
            final EditText longitudetext = (EditText) findViewById(R.id.longitude);
            final EditText altitudetext = (EditText)findViewById(R.id.altitude);


            if (post == null) {

                dateString.setText(DateConverter.dateToString(Calendar.getInstance()));

                if (locationArray != null) {
                    latitudetext.setText(Double.toString(locationArray[0]));
                    longitudetext.setText(Double.toString(locationArray[1]));
                    altitudetext.setText(Double.toString(locationArray[2]));
                }

                moodPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, TakePhotoActivity.class);
                        startActivityForResult(intent,TAKE_IMAGE_REQUEST);
                    }
                });
                ImageButton dateButton = (ImageButton) findViewById(R.id.datebutton);
                dateButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Context context = view.getContext();
                        Intent intent = new Intent(context, SelectDateActivity.class);
                        startActivityForResult(intent,REQUEST_GET_DATE);
                    }
                });
                final ImageButton deletePhoto = (ImageButton) findViewById(R.id.delimage);
                deletePhoto.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        moodPhoto.setImageDrawable(defaultImage);
                        deletePhoto.setVisibility(View.INVISIBLE);
                        image = null;
                    }
                });

                Button postButton = (Button) findViewById(R.id.postButton);
                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // condition checkers for fields that are not supposed to be null
                        if (textExplanation.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Want to say something?", Toast.LENGTH_LONG).show();
                        } else if (onRadioButtonClicked(moodButtons) == 8) {
                            Toast.makeText(getApplicationContext(), "Want to select a mood?", Toast.LENGTH_LONG).show();
                        } else if (textTrigger.getText().toString().equals("") && (image == null)) {
                            Toast.makeText(getApplicationContext(), "Want to say why?", Toast.LENGTH_LONG).show();
                        }
                        else if (onStatusButtonClicked(statusButtons) == 4) {
                            Toast.makeText(getApplicationContext(), "Want to select a social context?", Toast.LENGTH_LONG).show();
                        } else {
                            Profile profile = LocalData.getSignedInProfile(getApplicationContext());
                            if (date == null){
                                date = Calendar.getInstance();
                            }

                            Post post = new Post(textExplanation.getText().toString().replace("\\s+$", ""), onRadioButtonClicked(moodButtons),
                                    textTrigger.getText().toString().replace("\\s+$", ""), image, onStatusButtonClicked(statusButtons),
                                    profile.getId(),locationArray, date);
                            PostController postController = new PostController();
                            postController.addOrUpdatePosts(post);
                            Intent intent1 = new Intent();
                            intent.putExtra("POST",post);
                            setResult(RESULT_OK,intent);


                            finish();
                        }
                    }

                });
            } else {

                moodPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, TakePhotoActivity.class);
                        startActivityForResult(intent,TAKE_IMAGE_REQUEST);
                    }
                });
                ImageButton dateButton = (ImageButton) findViewById(R.id.datebutton);
                dateButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Context context = view.getContext();
                        Intent intent = new Intent(context, SelectDateActivity.class);
                        startActivityForResult(intent,REQUEST_GET_DATE);

                    }
                });
                final ImageButton deletePhoto = (ImageButton) findViewById(R.id.delimage);
                deletePhoto.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        moodPhoto.setImageDrawable(defaultImage);
                        deletePhoto.setVisibility(View.INVISIBLE);
                        image = null;
                    }
                });



                if (post.getTriggerImage() != null) {
                    Bitmap bitmap = ImageConverter.toBitmap(post.getTriggerImage());
                    this.moodPhoto.setImageBitmap(bitmap);
                    deletePhoto.setVisibility(View.VISIBLE);
                    image = post.getTriggerImage();
                }

                String oldExplannation = post.getText();
                String oldTrigger = post.getTriggerText();
                int oldmood = post.getMood();
                int oldcontext = post.getContext();
                Calendar olddate = post.getDate();

                double[] oldlocationArray = post.getLocation();

                String oldLatitude = null;
                String oldLongitude = null;
                String oldAltitude = null;


                if (oldlocationArray == null) {

                    if (locationArray != null) {
                        oldLatitude = Double.toString(locationArray[0]);
                        oldLongitude = Double.toString(locationArray[1]);
                        oldAltitude = Double.toString(locationArray[2]);
                    }
                }

                else {
                    oldLatitude = Double.toString(oldlocationArray[0]);
                    oldLongitude = Double.toString(oldlocationArray[1]);
                    oldAltitude = Double.toString(oldlocationArray[2]);
                }


                textTrigger.setText(oldTrigger);
                textExplanation.setText(oldExplannation);
                moodButtons.check(RadioConverter.getMoodButtonId(oldmood));
                statusButtons.check(RadioConverter.getContextButtonId(oldcontext));
                dateString.setText(DateConverter.dateToString(olddate));

                if (oldLatitude != null) latitudetext.setText(oldLatitude);
                altitudetext.setText(oldAltitude);
                longitudetext.setText(oldLongitude);



                Button postButton = (Button) findViewById(R.id.postButton);
                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //condition checkers for the input fields that are not supposed to be bull
                        if (textExplanation.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Want to say something?", Toast.LENGTH_LONG).show();
                        } else if (onRadioButtonClicked(moodButtons) == 8) {
                            Toast.makeText(getApplicationContext(), "Want to select a mood?", Toast.LENGTH_LONG).show();
                        } else if (textTrigger.getText().toString().equals("") && image == null) {
                            Toast.makeText(getApplicationContext(), "Want to say why?", Toast.LENGTH_LONG).show();
                        } else if (onStatusButtonClicked(statusButtons) == 4) {
                            Toast.makeText(getApplicationContext(), "Want to select a social context?", Toast.LENGTH_LONG).show();
                        } else {
                            post.setMood(onRadioButtonClicked(moodButtons));
                            post.setContext(onStatusButtonClicked(statusButtons));
                            post.setText(textExplanation.getText().toString().replace("\\s+$", ""));
                            post.setTriggerText(textTrigger.getText().toString().replace("\\s+$", ""));
                            post.setTriggerImage(image);
                            if (date != null)
                            post.setDate(date);


                            PostController postController = new PostController();
                            postController.addOrUpdatePosts(post);

                            Intent intent1 = new Intent();
                            intent.putExtra("POST",post);
                            setResult(RESULT_OK,intent);
                            finish();

                        }


                    }

                });
            }

        }





        //a back button
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
                 //return this int when no radiobutton is selected
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
                //returns this when no radiobutton is selected
                return 4;
        }
        return status;
    }
    // methods for checking internet accessibility
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






}