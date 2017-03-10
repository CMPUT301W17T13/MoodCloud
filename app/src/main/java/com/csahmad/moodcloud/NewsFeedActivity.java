package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

/** The activity for viewing the latest mood events from people the signed in user follows. */
public class NewsFeedActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutMananger;
    PostController postController = new PostController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        mRecyclerView = (RecyclerView) findViewById(R.id.postList);

        mRecyclerView.setHasFixedSize(TRUE);

        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);

        mAdapter = new MyAdapter(postController.getPosts(myDataset));
        mRecyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Post> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public TextView mNameView;
            public TextView mMoodView;
            public Post mPost;
            public Profile mProfile;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                mNameView = (TextView) v.findViewById(R.id.postName);
                mMoodView = (TextView) v.findViewById(R.id.postMood);
            }
        }

        public MyAdapter(ArrayList<Post> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_news_feed, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mPost = mDataset.get(position);
            holder.mProfile = holder.mPost.getPoster();
            holder.mNameView.setText(holder.mProfile.getName());
            holder.mMoodView.setText(holder.mPost.getMood());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
