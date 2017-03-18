package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
//import android.database.DataSetObserver;
import android.media.Image;
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

//import static java.lang.Boolean.TRUE;
//mwschafe commented out unused import statements

/** The activity for viewing the latest mood events from people the signed in user follows.
 * @author Taylor
 */
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

        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);

        try{
            ArrayList<Post> mDataset = postController.getFolloweePosts(LocalData.getSignedInProfile(),null,0);
            mAdapter = new MyAdapter(mDataset);
            mRecyclerView.setAdapter(mAdapter);
        } catch (TimeoutException e){
            System.err.println("TimeoutException: " + e.getMessage());
        }

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
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

        Button followingButton = (Button) findViewById(R.id.followingButton);
        followingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, FollowingActivity.class);
                startActivity(intent);
            }
        });

        Button profileButton = (Button) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("ID",LocalData.getSignedInProfile().getId());
                startActivity(intent);
            }
        });
    }

    /**
     * MyAdapter controls the list of newsfeed posts by extending RecyclerView <br>
     *     http://www.androidhive.info/2016/01/android-working-with-recycler-view/ <br>
     *         2017-03-7
     * @author Taylor
     */
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_feed_item, parent, false);

//                  ViewHolder vh = new ViewHolder(v);
//                  return vh;
//                  mwschafe fixing redudant variable from code above to code below
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position){

            Post post = mDataset.get(position);
            Profile profile = null;
            try {
                profile = new ProfileController().getProfileFromID(post.getPosterId());
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
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
