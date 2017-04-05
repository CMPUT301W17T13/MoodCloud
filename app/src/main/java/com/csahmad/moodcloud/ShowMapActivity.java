package com.csahmad.moodcloud;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;


public class ShowMapActivity extends AppCompatActivity
implements OnMapReadyCallback{

    PostController postController = new PostController();
    ArrayList<Post> posts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                finish();
            }}
        );

        SearchFilter filter = getIntent().getParcelableExtra("FILTER");
        String string = getIntent().getStringExtra("WHERE");
        if (string == null) string = "Everyone";

        switch (string) {

            case "Everyone":

                try {
                    this.posts = this.postController.getPosts(filter, 0);
                }

                catch (TimeoutException e) {
                    ConnectionManager.showConnectionError(this);
                }

                break;

            case "Following":

                try {
                    this.posts = this.postController.getLatestFolloweePosts(
                            LocalData.getSignedInProfile(this), filter, 0);
                }

                catch (TimeoutException e) {
                    ConnectionManager.showConnectionError(this);
                }

                break;

            case "My Moods":

                try {
                    this.posts = this.postController.getPosts(
                            LocalData.getSignedInProfile(this), filter, 0);
                }

                catch (TimeoutException e) {
                    ConnectionManager.showConnectionError(this);
                }

                break;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Modified from:
    // https://stackoverflow.com/questions/19076124/android-map-marker-color/33036461#33036461
    // (StackOverflow user S.Thiongane, edited by user1732111)
    // Accessed April 5, 2017
    /**
     * Convert the given colour from hex format to HSV format and return the hue.
     *
     * @param hexColor the colour to convert
     * @return the hue of the given colour
     */
    private static float getHue(String hexColor) {

        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(hexColor), hsv);
        return hsv[0];
    }

    // Modified from:
    // https://stackoverflow.com/questions/19076124/android-map-marker-color/33036461#33036461
    // (StackOverflow user S.Thiongane, edited by user1732111)
    // Accessed April 5, 2017
    /**
     * Return the default map marker icon in the given colour.
     *
     * @param hexColor the colour of the returned marker icon
     * @return the default map marker icon in the given colour
     */
    private static BitmapDescriptor getMarkerIcon(String hexColor) {

        return BitmapDescriptorFactory.defaultMarker(ShowMapActivity.getHue(hexColor));
    }

    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = null;
        double[] location;
        ProfileController profileController = new ProfileController();
        Profile poster = null;
        MarkerOptions marker;
        BitmapDescriptor markerIcon;
        String snippet;

        for (Post post: this.posts) {

            location = post.getLocation();

            if (post.getLocation() != null) {

                latLng = new LatLng(location[0], location[1]);

                markerIcon = ShowMapActivity.getMarkerIcon(
                        Mood.toHexColor(post.getMood(), this));

                marker = new MarkerOptions()
                        .position(latLng)
                        .title(post.getText())
                        .icon(markerIcon);

                try {
                    poster = profileController.getProfileFromID(post.getPosterId());
                }

                catch (Exception e) {}

                snippet = Mood.toString(post.getMood());

                if (poster != null)
                    snippet += " " + poster.getName();

                marker.snippet(snippet);

                googleMap.addMarker(marker);
            }
        }

        if (latLng != null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}