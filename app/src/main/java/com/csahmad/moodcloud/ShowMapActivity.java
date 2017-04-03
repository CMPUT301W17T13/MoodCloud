package com.csahmad.moodcloud;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        SearchFilter filter = getIntent().getParcelableExtra("FILTER");
        String string = getIntent().getStringExtra("WHERE");

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
                ;
                break;

            case "My Moods":
                ;
                break;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}