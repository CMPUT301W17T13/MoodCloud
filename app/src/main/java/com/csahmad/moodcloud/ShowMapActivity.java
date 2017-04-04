package com.csahmad.moodcloud;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = null;
        double[] location;

        for (Post post: this.posts) {

            location = post.getLocation();

            if (post.getLocation() != null) {

                latLng = new LatLng(location[0], location[1]);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(post.getText()));
            }
        }

        if (latLng != null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}