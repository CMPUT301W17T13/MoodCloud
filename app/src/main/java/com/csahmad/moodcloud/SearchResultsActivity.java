package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by Taylor on 2017-03-29.
 */

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutMananger;
    PostController postController = new PostController();
    private int loadCount = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItems, visibleItemCount, totalItemCount;
    ArrayList<Post> mDataset;
    private String where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        mRecyclerView = (RecyclerView) findViewById(R.id.resultList);

        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);
        Intent intent = getIntent();
        where = intent.getStringExtra("WHERE");
        final SearchFilter filter = intent.getParcelableExtra("FILTER");
        TextView maxMood = (TextView) findViewById(R.id.maxMood);
        HashMap<Integer, Long> moodCounts;
        try {
            if (where.equals("Following")){
                moodCounts = postController.getFolloweeMoodCounts(
                        filter, LocalData.getSignedInProfile(getApplicationContext()));
            } else {
                if (where.equals("My Moods")) {
                    moodCounts = postController.getMoodCounts(filter,
                            LocalData.getSignedInProfile(getApplicationContext()));
                } else {
                    moodCounts = postController.getMoodCounts(filter);
                }
            }
            if (filter.hasMood() == FALSE) {
                Long maxCount = (Collections.max(moodCounts.values()));
                for (Map.Entry<Integer, Long> entry : moodCounts.entrySet()) {
                    if (entry.getValue() == maxCount){
                        String[] draws = new String[]{"Angry","Confused","Disgusted","Fear","Happy","Sad","Shame","Suprised"};
                        maxMood.setText(draws[entry.getKey()] + " is the Most Common Mood");
                        break;
                    }
                }
            }
            if (filter.hasMood() == TRUE){
                String count = moodCounts.get(filter.getMood()).toString();
                maxMood.setText(count + " Posts");
            }
        }catch (TimeoutException e) {}
        try {
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
            mAdapter = new SearchResultsActivity.MyAdapter(mDataset);
            mRecyclerView.setAdapter(mAdapter);
        }catch (TimeoutException e) {}

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }}
        );

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
                        ArrayList<Post> newDS;
                        if (where.equals("Following")) {
                            newDS = postController.getFolloweePosts(
                                    LocalData.getSignedInProfile(getApplicationContext()),
                                    filter, loadCount);
                        } else {
                            if (where.equals("My Moods")) {
                                newDS = postController.getPosts(
                                        LocalData.getSignedInProfile(getApplicationContext()),
                                        filter, loadCount);
                            } else {
                                newDS = postController.getPosts(filter, loadCount);
                            }
                        }
                        mDataset.addAll(newDS);
                    } catch (TimeoutException e) {}
                    mAdapter.notifyDataSetChanged();
                    loading = true;
                }
            }
        });
    }

    /**
     * MyAdapter controls the list of newsfeed posts by extending RecyclerView <br>
     *     http://www.androidhive.info/2016/01/android-working-with-recycler-view/ <br>
     *         2017-03-7
     * @author Taylor
     */
    public class MyAdapter extends RecyclerView.Adapter<SearchResultsActivity.MyAdapter.ViewHolder> {
        private ArrayList<Post> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView mNameView;
            public ImageView mMoodView;
            public TextView mTextView;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.postText);
                mNameView = (TextView) v.findViewById(R.id.postName);
                mMoodView = (ImageView) v.findViewById(R.id.postMood);
                v.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                int position = mRecyclerView.getChildLayoutPosition(view);
                Post post = mDataset.get(position);
                Context context = view.getContext();
                Intent intent = new Intent(context, ViewPostActivity.class);
                intent.putExtra("POST",post);
                startActivity(intent);
            }
        }

        public MyAdapter(ArrayList<Post> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public SearchResultsActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_results_item, parent, false);

//                  ViewHolder vh = new ViewHolder(v);
//                  return vh;
//                  mwschafe fixing redudant variable from code above to code below
            return new SearchResultsActivity.MyAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SearchResultsActivity.MyAdapter.ViewHolder holder, int position){

            Post post = mDataset.get(position);
            Profile profile = null;
            try {
                profile = new ProfileController().getProfileFromID(post.getPosterId());
                if (profile == null) profile = Profile.dummy;
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            holder.mNameView.setText(profile.getName());
            holder.mTextView.setText(post.getText());
            int[] draws = new int[]{R.drawable.angry,R.drawable.confused,R.drawable.disgusted,
                    R.drawable.fear,R.drawable.happy,R.drawable.sad,R.drawable.shame,R.drawable.suprised};
            holder.mMoodView.setImageResource(draws[post.getMood()]);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
