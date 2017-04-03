package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class ShowMapActivity extends AppCompatActivity{

    /*

    private PostController postController = new PostController();
    private ProfileController profileController = new ProfileController();
    private Profile profile;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutMananger;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        //String id = intent.getStringExtra("ID");
        String where = intent.getStringExtra("WHERE");
        final SearchFilter filter = intent.getParcelableExtra("FILTER");
        try {
            profile = profileController.getProfileFromID(id);

            if (profile.getId() == null)
                throw new RuntimeException("Oh noes ID is null");

        } catch (TimeoutException e){}
        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }}
        );

        ImageButton addPost = (ImageButton) findViewById(R.id.addPost);
        addPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddOrEditPostActivity.class);
                startActivity(intent);
            }
        });
        ImageButton searchButton = (ImageButton) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, SearchMoods.class);
                startActivity(intent);
            }}
        );

        try {

            final ArrayList<Post> mDataset;
            if (where.equals("Following")) {
                mDataset = postController.getFolloweePosts(
                        LocalData.getSignedInProfile(getApplicationContext()),
                        filter, 0);
            } else {
                if (where.equals("My Moods")) {
                    mDataset = postController.getPosts(
                            LocalData.getSignedInProfile(getApplicationContext()),
                            filter, 0);
                } else {
                    mDataset = postController.getPosts(filter, 0);
                }
            }
            mAdapter = new ViewProfileActivity.MyAdapter(mDataset);

            double[] location = mDataset.get(0).getLocation();

            if (location != null) {
                double lat = location[0];
                double lo = location[1];
                LatLng mood = new LatLng(lat,lo);
                String text = mDataset.get(0).getText().toString();
                setMap(mood,googleMap,text);
            }

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(new ViewProfileActivity.RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ViewProfileActivity.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Post post = mDataset.get(position);
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ViewPostActivity.class);
                    intent.putExtra("POST_ID",post.getId());
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }



    }
    public void setMap(LatLng latLng,GoogleMap googleMap,String text){

        if (latLng == null) return;
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(text));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Edmonton Alberta,
        // and move the map's camera to the same location.
        LatLng ETLC = new LatLng(53.527266, -113.529666);
        googleMap.addMarker(new MarkerOptions().position(ETLC)
                .title("ETLC University Of Alberta"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ETLC));


    }

    */
}