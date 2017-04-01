package com.csahmad.moodcloud;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SearchMoods extends AppCompatActivity {

    boolean recent = FALSE;
    boolean locperm = FALSE;
    boolean near = FALSE;
    private static final int READ_LOCATION_REQUEST = 1;
    private SearchFilter filter;


    //https://developer.android.com/guide/topics/ui/controls/checkbox.html
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.recentBox:
                if (checked){
                    recent = TRUE;
                }
                break;
            case R.id.nearBox:
                if (checked) {
                    recent = TRUE;
                }
                break;
        }
    }

    /** Request permission from the user to access location. */
    public void requestLocationPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                READ_LOCATION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                              int[] grantResults) {

        if (requestCode == READ_LOCATION_REQUEST)
            this.setFilterLocation();
    }

    private void setFilterLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double altitude = location.getAltitude();

                locperm = TRUE;

                if (near == TRUE) {
                    this.filter.setMaxDistance(5.0d);
                    this.filter.setLocation(new SimpleLocation(latitude, longitude, altitude));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_moods);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText findText = (EditText) findViewById(R.id.findText);

        final Spinner topSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> topAdapter = ArrayAdapter.createFromResource(this,
                R.array.whereArray, android.R.layout.simple_spinner_item);
        topAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topSpinner.setAdapter(topAdapter);

        final Spinner moodSpinner = (Spinner) findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> moodAdapter = ArrayAdapter.createFromResource(this,
                R.array.moodsArray, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        moodSpinner.setAdapter(moodAdapter);

        final Spinner contextSpinner = (Spinner) findViewById(R.id.spinner3);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> contextAdapter = ArrayAdapter.createFromResource(this,
                R.array.groupSoloArray, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        contextSpinner.setAdapter(contextAdapter);

        Button searchButton = (Button) findViewById(R.id.searchButton);

        final SearchMoods context = this;



        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String where = (String) topSpinner.getSelectedItem();
                String keywordString = findText.getText().toString().trim();
                String moodString = (String) moodSpinner.getSelectedItem();
                String contextString = (String) contextSpinner.getSelectedItem();

                SearchFilter filter = new SearchFilter();
                context.filter = filter;

                if (!keywordString.equals("")) {
                    filter.addKeywordField("triggerText");
                    String[] keywords = keywordString.split("\\s+|\\s*,\\s*");
                    for (String keyword: keywords) filter.addKeyword(keyword);
                }

                if (!moodString.equals("Any"))
                    filter.setMood(Mood.fromString(moodString));

                if (!contextString.equals("Any")) {
                    filter.setContext(SocialContext.fromString(contextString));
                }

                Intent intent = new Intent(context, SearchResultsActivity.class);

                if (recent == TRUE) {
                    filter.setMaxTimeUnitsAgo(1);
                }

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

                    requestLocationPermission();
                }

                else
                    setFilterLocation();

                intent.putExtra("WHERE", where);
                intent.putExtra("FILTER", filter);
                startActivity(intent);
            }
        });

        Button findUserButton = (Button) findViewById(R.id.searchUsers);
        final EditText findUser = (EditText) findViewById(R.id.findUser);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }}
        );

        findUserButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    String input = findUser.getText().toString();
                    ProfileController controller = new ProfileController();
                    Profile profile = controller.getProfileFromUsername(input);

                    if (profile == null)
                        Toast.makeText(context, "No such user", Toast.LENGTH_LONG).show();

                    else {
                        Intent intent = new Intent(context, ViewProfileActivity.class);
                        intent.putExtra("ID", profile.getId());
                        startActivity(intent);
                    }
                }

                catch (TimeoutException e) {
                    Log.i("Error", "Oh no");
                }
            }
        });



    }

}
