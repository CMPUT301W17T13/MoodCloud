package com.csahmad.moodcloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/** The main activity.
 *
 * called upon launch to determine which activity to go to first:
 * {@link NewsFeedActivity} if a user is already signed in,
 * {@link SignInActivity} if no user is signed in
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.updatePosts();

        if (LocalData.getSignedInProfile(getApplicationContext()) == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            this.startActivity(intent);
        }

        else {
            Intent intent = new Intent(this, NewsFeedActivity.class);
            this.startActivity(intent);
        }
    }

    /**
     * Check if there are any local {@link Post}s that need to be updated/deleted and try to
     * update/delete them online.
     */
    /*private void updatePosts() {

        if (!ConnectionManager.haveConnection(this)) {
            Toast.makeText(getApplicationContext(), "Sync failed (no connection)", Toast.LENGTH_LONG).show();
            return;
        }

        PostController controller = new PostController();

        ArrayList<Post> postList = LocalData.getToUpdate(this);
        Post[] posts = null;
        if (postList != null) posts = (Post[]) postList.toArray();

        if (posts != null) {

            Toast.makeText(getApplicationContext(), "Syncing updated posts", Toast.LENGTH_LONG).show();

            try {
                controller.addOrUpdatePosts(posts);
                controller.waitForTask();
                LocalData.clearToUpdate(this);
            }

            catch (TimeoutException e) {
                Toast.makeText(getApplicationContext(), "Sync failed (timeout)", Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sync failed", Toast.LENGTH_LONG).show();
            }
        }

        postList = LocalData.getToDelete(this);
        posts = null;
        if (postList != null) posts = (Post[]) postList.toArray();

        if (posts != null) {

            Toast.makeText(getApplicationContext(), "Syncing deleted posts", Toast.LENGTH_LONG).show();

            try {
                controller.deletePosts(posts);
                controller.waitForTask();
                LocalData.clearToDelete(this);
            }

            catch (TimeoutException e) {
                Toast.makeText(getApplicationContext(), "Sync failed (timeout)", Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sync failed", Toast.LENGTH_LONG).show();
            }
        }

        postList = LocalData.getToAdd(this);
        posts = null;
        if (postList != null) posts = (Post[]) postList.toArray();

        if (posts != null) {

            Toast.makeText(getApplicationContext(), "Syncing adding posts", Toast.LENGTH_LONG).show();

            try {
                controller.addOrUpdatePosts(posts);
                controller.waitForTask();
                LocalData.clearToAdd(this);
            }

            catch (TimeoutException e) {
                Toast.makeText(getApplicationContext(), "Sync failed (timeout)", Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sync failed", Toast.LENGTH_LONG).show();
            }
        }
        try{
        LocalData.setUserPosts(controller.getPosts(LocalData.getSignedInProfile(getApplicationContext()),null,0));
    }catch (TimeoutException e) {}}*/
}

