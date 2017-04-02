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
    private RecyclerView.LayoutManager mLayoutMananger;
    private ProfileController profileController = new ProfileController();
    //mwschafe made ProfileController private

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        mRecyclerView = (RecyclerView) findViewById(R.id.followerList);

        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);

        try{
            final ArrayList<Profile> mDataset = profileController.getFollowees(LocalData.getSignedInProfile(getApplicationContext()),0);
            mAdapter = new FollowingActivity.MyAdapter(mDataset);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Profile profile = mDataset.get(position);
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ViewProfileActivity.class);
                    intent.putExtra("ID",profile.getId());
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } catch (TimeoutException e){
            System.err.println("TimeoutException: " + e.getMessage());
        }

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //erick 2017-04-01 set signedinprofile to null before signing out
                LocalData.store((Profile) null, getApplicationContext());
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

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mNameView;

            public ViewHolder(View v) {
                super(v);
                mNameView = (TextView) v.findViewById(R.id.followerName);
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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FollowingActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FollowingActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
