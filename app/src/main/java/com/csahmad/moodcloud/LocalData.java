package com.csahmad.moodcloud;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-01.
 */

public class LocalData {

    // If null, try to read from filesystem
    private static Profile signedInProfile;
    private static final String PROFILESAVE = "profile.sav";
    private static final String POSTSAVE = "posts.sav";

    public static Profile getSignedInProfile(Context context) {

        if (LocalData.signedInProfile == null)
            LocalData.tryReadProfile(context);

        return LocalData.signedInProfile;
    }

    // Call getProfile and store result in file system and signedInProfile
    // update 2017-03-20 store posts of signedInProfile now that they are no longer
    // contained in the profile object
    public static void store(Profile profile, Context context) {

        PostController pc = new PostController();
        ArrayList<Post> userPosts;
        LocalData.signedInProfile = profile;

        //get posts
        try{
            userPosts = pc.getPosts(profile, null, 0);
        }
        catch(TimeoutException e){
            userPosts = new ArrayList<Post>();
        }

        //Store data
        try{
            //Store Profile to PROFILESAVE file
            FileOutputStream fos = context.openFileOutput(PROFILESAVE, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(profile, out);
            out.flush();
            fos.close();
            //Store Posts to POSTSAVE file
            fos = context.openFileOutput(POSTSAVE, Context.MODE_PRIVATE);
            out = new BufferedWriter(new OutputStreamWriter(fos));
            gson = new Gson();
            gson.toJson(userPosts, out);
            out.flush();
            fos.close();

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // Try getting from file system and storing in signedInProfile
    // Return false if not stored
    public static boolean tryReadProfile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(PROFILESAVE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            Type listType = new TypeToken<Profile>() {
            }.getType();
            signedInProfile = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return false;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        return true;
    }


}
