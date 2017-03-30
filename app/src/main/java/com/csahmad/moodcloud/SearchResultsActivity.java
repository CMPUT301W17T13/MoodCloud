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
import java.util.concurrent.TimeoutException;

/**
 * Created by Taylor on 2017-03-29.
 */

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutMananger;
    PostController postController = new PostController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        mRecyclerView = (RecyclerView) findViewById(R.id.resultList);

        mLayoutMananger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutMananger);
        Intent intent = getIntent();
        String where = intent.getStringExtra("WHERE");
        final SearchFilter filter = intent.getParcelableExtra("FILTER");
            //Toast.makeText(getApplicationContext(), "filter null", Toast.LENGTH_LONG).show();

        PostController postController = new PostController();
        final ArrayList<Post> mDataset;
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
            mRecyclerView.addOnItemTouchListener(new SearchResultsActivity.RecyclerTouchListener(getApplicationContext(), mRecyclerView, new SearchResultsActivity.ClickListener() {
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
        }catch (TimeoutException e) {}

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }}
        );
    }

    /**
     * MyAdapter controls the list of newsfeed posts by extending RecyclerView <br>
     *     http://www.androidhive.info/2016/01/android-working-with-recycler-view/ <br>
     *         2017-03-7
     * @author Taylor
     */
    public class MyAdapter extends RecyclerView.Adapter<SearchResultsActivity.MyAdapter.ViewHolder> {
        private ArrayList<Post> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mNameView;
            public ImageView mMoodView;
            public TextView mTextView;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.postText);
                mNameView = (TextView) v.findViewById(R.id.postName);
                mMoodView = (ImageView) v.findViewById(R.id.postMood);
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
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            holder.mNameView.setText(profile.getName());
            holder.mTextView.setText(post.getText());
            int[] draws = new int[]{R.drawable.angry,R.drawable.confused,R.drawable.disgusted,
                    R.drawable.embarassed,R.drawable.fear,R.drawable.happy,R.drawable.sad,R.drawable.shame,R.drawable.suprised};
            holder.mMoodView.setImageResource(draws[post.getMood()]);
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
        private SearchResultsActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SearchResultsActivity.ClickListener clickListener) {
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
