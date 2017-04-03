package com.csahmad.moodcloud;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/** The activity for viewing people the signed in user is following.
 * @author Taylor
 */
public class FollowingActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutMananger;
    private ProfileController profileController = new ProfileController();
    private int loadCount = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItems, visibleItemCount, totalItemCount;
    ArrayList<Profile> mDataset;
    //mwschafe made ProfileController private

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        mRecyclerView = (RecyclerView) findViewById(R.id.followerList);

        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);

        try{
            mDataset = profileController.getFollowees(LocalData.getSignedInProfile(getApplicationContext()),0);
            mAdapter = new FollowingActivity.MyAdapter(mDataset);
            mRecyclerView.setAdapter(mAdapter);
        } catch (TimeoutException e){
            System.err.println("TimeoutException: " + e.getMessage());
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutMananger.getChildCount();
                totalItemCount = mLayoutMananger.getItemCount();
                firstVisibleItems = mLayoutMananger.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <=
                        (firstVisibleItems + visibleThreshold)) {
                    loadCount = loadCount + ElasticSearchController.getResultSize();
                    try {
                        ArrayList<Profile> newDS = profileController.getFollowees(LocalData.getSignedInProfile(getApplicationContext()),loadCount);
                        mDataset.addAll(newDS);
                    } catch (TimeoutException e) {}
                    mAdapter.notifyDataSetChanged();
                    loading = true;
                }
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //erick 2017-04-01 set signedinprofile to null before signing out
                LocalData.store((Account) null, getApplicationContext());
                Context context = view.getContext();
                Intent intent = new Intent(context, SignInActivity.class);
                startActivity(intent);
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

        Button followingButton = (Button) findViewById(R.id.newsfeedButton);
        followingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, NewsFeedActivity.class);
                startActivity(intent);
            }
        });

        Button profileButton = (Button) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("ID",LocalData.getSignedInProfile(getApplicationContext()).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * MyAdapter controls the list of profiles the signed in user is following by extending RecyclerView <br>
     *     http://www.androidhive.info/2016/01/android-working-with-recycler-view/ <br>
     *         2017-03-7
     * @author Taylor
     */
    public class MyAdapter extends RecyclerView.Adapter<FollowingActivity.MyAdapter.ViewHolder> {
        private ArrayList<Profile> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView mNameView;

            public ViewHolder(View v) {
                super(v);
                mNameView = (TextView) v.findViewById(R.id.followerName);
                v.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                int position = mRecyclerView.getChildLayoutPosition(view);
                Profile profile = mDataset.get(position);
                Context context = view.getContext();
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("ID",profile.getId());
                startActivity(intent);
            }
        }

        public MyAdapter(ArrayList<Profile> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public FollowingActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.following_item, parent, false);

//            FollowingActivity.MyAdapter.ViewHolder vh = new FollowingActivity.MyAdapter.ViewHolder(v);
//            return vh;
//            mwschafe fixing redudant variable from code above to code below
                    return new FollowingActivity.MyAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final FollowingActivity.MyAdapter.ViewHolder holder, int position) {
            Profile profile= mDataset.get(position);
            holder.mNameView.setText(profile.getName());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
