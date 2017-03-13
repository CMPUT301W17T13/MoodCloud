package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/** The activity for viewing the {@link Profile} of a user and the mood history of that user. */
public class ViewProfileActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutMananger;
    private Profile profile;
    private ProfileController profileController = new ProfileController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        mRecyclerView = (RecyclerView) findViewById(R.id.profilePostList);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        try {
            profile = profileController.getProfileFromID(id);

            if (profile.getId() == null)
                throw new RuntimeException("Oh noes ID is null");

        } catch (TimeoutException e){}
        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);
        TextView nameText = (TextView) findViewById(R.id.profileName);
        nameText.setText("Name: " + profile.getName());
        ArrayList<Post> mDataset = profile.getPosts();
        mAdapter = new ViewProfileActivity.MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);


        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Context context = view.getContext();
                Intent intent = new Intent(context, SignInActivity.class);
                startActivity(intent);
            }}
        );

        Button followingButton = (Button) findViewById(R.id.followingButton);
        followingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, FollowingActivity.class);
                startActivity(intent);
            }
        });

        Button profileButton = (Button) findViewById(R.id.newsfeedButton);
        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, NewsFeedActivity.class);
                startActivity(intent);
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<ViewProfileActivity.MyAdapter.ViewHolder> {
        private ArrayList<Post> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mNameView;
            public TextView mMoodView;
            public TextView mTextView;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.postText);
                mNameView = (TextView) v.findViewById(R.id.postName);
                mMoodView = (TextView) v.findViewById(R.id.postMood);
            }
        }

        public MyAdapter(ArrayList<Post> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public ViewProfileActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_post_item, parent, false);

            ViewProfileActivity.MyAdapter.ViewHolder vh = new ViewProfileActivity.MyAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewProfileActivity.MyAdapter.ViewHolder holder, int position) {
            Post post = mDataset.get(position);
            Profile profile = post.getPoster();
            holder.mNameView.setText(profile.getName());
            holder.mTextView.setText(post.getText());
            holder.mMoodView.setText(post.getMood());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
