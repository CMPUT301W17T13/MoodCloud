package com.csahmad.moodcloud;

import android.Manifest;
import android.app.Activity;
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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;
import android.location.LocationManager;
import com.google.android.gms.location.LocationRequest;

/** The activity for adding a {@link Post} or editing an existing one. */
public class AddOrEditPostActivity extends AppCompatActivity {

    public static final int READ_CAMERA_REQUEST = 0;
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
    EditText latitudetext = null; //(EditText) findViewById(R.id.latitude);
    EditText longitudetext = null; //(EditText) findViewById(R.id.longitude);
    EditText altitudetext = null; //(EditText)findViewById(R.id.altitude);



    @Override
    // Methods to get the result from the camera and the date picker.
    //Cases are separated by REQUESTS that are ints
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == TAKE_IMAGE_REQUEST) {

            if (resultCode == RESULT_OK) {

                Bundle extras = intent.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                if (imageBitmap != null) {
                    this.image = ImageConverter.toString(imageBitmap);
                    Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                    this.moodPhoto.setImageBitmap(bitmap);
                    deletePhoto.setVisibility(View.VISIBLE);
                }

                moodPhoto.setImageBitmap(imageBitmap);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("IMAGE", ImageConverter.toString(imageBitmap));
                returnIntent.putExtra("BITMAP", imageBitmap);
                setResult(Activity.RESULT_OK, returnIntent);

            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Photo was cancelled!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Unknown bug! Please report!", Toast.LENGTH_LONG).show();
        }

        else if (requestCode == REQUEST_GET_DATE){
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

                setLocation();
            }
        }

        else if (requestCode == READ_CAMERA_REQUEST) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_IMAGE_REQUEST);
            }
        }
    }

    private void setLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            String provider = lm.getBestProvider(new Criteria(), true);

            if (!lm.isProviderEnabled(provider)) {
                Log.i("LocationStatus", "Provider disabled");
            }

            Location location = lm.getLastKnownLocation(provider);

            if (location == null){

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationListener locationListener= new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {

                        Log.i("LocationStatus", "Got location!");
                        locationArray = new double[]{location.getLatitude(),
                                location.getLongitude(), location.getAltitude()};
                        DecimalFormat format = new DecimalFormat("#.####");
                        latitudetext.setText(format.format(locationArray[0]));
                        longitudetext.setText(format.format(locationArray[1]));
                        altitudetext.setText(format.format(locationArray[2]));

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                        if (status == LocationProvider.OUT_OF_SERVICE) {
                            Log.i("LocationStatus", "Out of service");
                        }

                        else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                            Log.i("LocationStatus", "Unavailable");
                        }

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                        ;
                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                        Log.i("LocationStatus", "Disabled");
                    }
                };

                //lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                lm.requestLocationUpdates(provider, 2_000l, 100f, locationListener);
            }

            else {
                Log.i("LocationStatus", "Got location!");
                locationArray = new double[]{location.getLatitude(),
                        location.getLongitude(), location.getAltitude()};
            }
        }

        else {
            Log.i("LocationStatus", "Denied!");
        }
    }

    /** Request permission from the user to use the camera. */
    public void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                READ_CAMERA_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_post);

        moodPhoto = (ImageButton) findViewById(R.id.moodPhoto);
        defaultImage = moodPhoto.getDrawable();
        dateString = (TextView) findViewById(R.id.postDate);
        deletePhoto = (ImageButton) findViewById(R.id.delimage);

        latitudetext = (EditText) findViewById(R.id.latitude);
        longitudetext = (EditText) findViewById(R.id.longitude);
        altitudetext = (EditText)findViewById(R.id.altitude);

        latitudetext.setFilters(new InputFilter[]{new InputFilterMinMax(0.0d, 90.0d)});
        longitudetext.setFilters(new InputFilter[]{new InputFilterMinMax(-180.0d, 180.0d)});



        //if(isNetworkAvailable()) {
            final Intent intent = getIntent();
            final Post post = intent.getParcelableExtra("POST");


            final EditText textExplanation = (EditText) findViewById(R.id.body);
            final EditText textTrigger = (EditText) findViewById(R.id.trigger);
            textTrigger.setFilters(new InputFilter[]{new TriggerInputFilter(20, 3)});

            final RadioGroup moodButtons = (RadioGroup) findViewById(R.id.moodRadioGroup);
            final RadioGroup statusButtons = (RadioGroup) findViewById(R.id.statusRadioGroup);



            if (post == null) {
                // TODO: 2017-03-31 Find out why Android Studio won't let me put this in a separate method
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i("LocationStatus", "Requesting permission");
                    requestLocationPermission();
                }

                else
                    setLocation();

                dateString.setText(DateConverter.dateToString(Calendar.getInstance()));

                if (locationArray != null) {
                    DecimalFormat format = new DecimalFormat("#.####");
                    latitudetext.setText(format.format(locationArray[0]));
                    longitudetext.setText(format.format(locationArray[1]));
                    altitudetext.setText(format.format(locationArray[2]));
                }

                final Context context = this;

                moodPhoto.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) !=
                                PackageManager.PERMISSION_GRANTED)
                            requestCameraPermission();

                        else  {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, TAKE_IMAGE_REQUEST);
                        }
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

                        Integer socialContext = onStatusButtonClicked(statusButtons);
                        if (socialContext == 4) socialContext = null;

                        // condition checkers for fields that are not supposed to be null
                        if (textExplanation.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Want to say something?", Toast.LENGTH_LONG).show();
                        } else if (onRadioButtonClicked(moodButtons) == 8) {
                            Toast.makeText(getApplicationContext(), "Want to select a mood?", Toast.LENGTH_LONG).show();
                        } else if (textTrigger.getText().toString().equals("") && (image == null)) {
                            Toast.makeText(getApplicationContext(), "Want to say why?", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Profile profile = LocalData.getSignedInProfile(getApplicationContext());
                            if (date == null){
                                date = Calendar.getInstance();
                            }

                            Post post = new Post(textExplanation.getText().toString().replace("\\s+$", ""), onRadioButtonClicked(moodButtons),
                                    textTrigger.getText().toString().replace("\\s+$", ""), image, socialContext,
                                    profile.getId(),locationArray, date);
                            if (isNetworkAvailable()){
                            PostController postController = new PostController();
                            postController.addOrUpdatePosts(post);} else{
                            LocalData.addPost(post, context);}
                            Intent intent = new Intent();
                            intent.putExtra("POST",post);
                            setResult(RESULT_OK,intent);


                            finish();
                        }
                    }

                });
            } else {

                final Context context = this;

                moodPhoto.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) !=
                                PackageManager.PERMISSION_GRANTED) {

                            Log.i("LocationStatus", "Requesting permission");
                            requestCameraPermission();
                        }

                        else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, TAKE_IMAGE_REQUEST);
                        }
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
                Integer oldcontext = post.getContext();
                Calendar olddate = post.getDate();

                double[] oldlocationArray = post.getLocation();

                Double oldLatitude = null;
                Double oldLongitude = null;
                Double oldAltitude = null;


                if (oldlocationArray == null) {

                    if (locationArray != null) {
                        oldLatitude = locationArray[0];
                        oldLongitude = locationArray[1];
                        oldAltitude = locationArray[2];
                    }
                }

                else {
                    oldLatitude = oldlocationArray[0];
                    oldLongitude = oldlocationArray[1];
                    oldAltitude = oldlocationArray[2];
                }


                textTrigger.setText(oldTrigger);
                textExplanation.setText(oldExplannation);
                moodButtons.check(RadioConverter.getMoodButtonId(oldmood));

                if (oldcontext != null)
                    statusButtons.check(RadioConverter.getContextButtonId(oldcontext));

                dateString.setText(DateConverter.dateToString(olddate));

                if (oldLatitude != null) {
                    DecimalFormat format = new DecimalFormat("#.####");
                    latitudetext.setText(format.format(oldLatitude));
                    altitudetext.setText(format.format(oldAltitude));
                    longitudetext.setText(format.format(oldLongitude));}



                Button postButton = (Button) findViewById(R.id.postButton);
                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer socialContext = onStatusButtonClicked(statusButtons);
                        if (socialContext == 4) socialContext = null;

                        //condition checkers for the input fields that are not supposed to be bull
                        if (textExplanation.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Want to say something?", Toast.LENGTH_LONG).show();
                        } else if (onRadioButtonClicked(moodButtons) == 8) {
                            Toast.makeText(getApplicationContext(), "Want to select a mood?", Toast.LENGTH_LONG).show();
                        } else if (textTrigger.getText().toString().equals("") && image == null) {
                            Toast.makeText(getApplicationContext(), "Want to say why?", Toast.LENGTH_LONG).show();
                        } else {
                            post.setMood(onRadioButtonClicked(moodButtons));
                            post.setContext(socialContext);
                            post.setText(textExplanation.getText().toString().replace("\\s+$", ""));
                            post.setTriggerText(textTrigger.getText().toString().replace("\\s+$", ""));
                            post.setTriggerImage(image);
                            if (date != null)
                            post.setDate(date);

                            if (isNetworkAvailable()){
                                PostController postController = new PostController();
                                postController.addOrUpdatePosts(post);}

                            Intent intent = new Intent();
                            intent.putExtra("POST",post);
                            setResult(RESULT_OK,intent);
                            finish();

                        }


                    }

                });
            }

        //}





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