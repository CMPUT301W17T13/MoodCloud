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
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-01.
 *
 * Stores an {@link Account} and {@link Post}s for the current user
 */

public class LocalData {

    // If null, try to read from filesystem
    private static Account signedInAccount;
    private static ArrayList<Post> userPosts;
    private static final String ACCOUNTSAVE = "account.sav";
    private static final String POSTSAVE = "posts.sav";

    /**
     * Used to access the {@link Account} of the current signed in user
     *
     * If no account is currently stored, returns null
     *
     * @param context
     * @return the current stored account.
     */
    public static Account getSignedInAccount(Context context){
        LocalData.tryReadProfile(context);
        return LocalData.signedInAccount;
    }

    /**
     * gets profile of stored account
     *
     * Returns null if no account is found in memory
     *
     * @param context
     * @return profile from memory
     */
    public static Profile getSignedInProfile(Context context) {

        LocalData.tryReadProfile(context);
        if(LocalData.signedInAccount != null) {
            return LocalData.signedInAccount.getProfile();
        }
        else{
            return null;
        }
    }

    /**
     * @param context current app context
     * @return posts of signed in user
     */
    public static ArrayList<Post> getUserPosts(Context context){
        tryReadPosts(context);
        return userPosts;
    }

    /**
     * Stores Account in local memory
     *
     * @param account account to be stored
     * @param context
     */
    public static void store(Account account, Context context) {

        //Store data
        try {
            //Store ACCOUNT to ACCOUNTSAVE file
            FileOutputStream fos = context.openFileOutput(ACCOUNTSAVE, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(account, out);
            out.flush();
            fos.close();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    /**
     * Reads account of signed in user
     *
     * @param context
     * @return true if Account is read
     */
    public static boolean tryReadProfile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(ACCOUNTSAVE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            Type listType = new TypeToken<Account>() {
            }.getType();
            signedInAccount = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return false;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        return true;
    }

    /**
     * Add the given {@link Post} to the local file.
     *
     * @param post the {@link Post} to add
     * @param context
     */
    public static void addPost(Post post, Context context) {

        userPosts.add(post);
        store(userPosts, context);
    }

    /**
     * Set the {@link Post} at the given index to the given {@link Post}.
     *
     * @param index where the old post is stored in userPosts
     * @param post the post to replace the old post with
     * @param context
     */
    public static void updatePostAt(int index, Post post, Context context) {

        userPosts.set(index, post);
        store(userPosts, context);
    }

    /**
     * Delete the {@link Post} at the given index and update the file.
     *
     * @param index the index where the {@link Post} is in userPosts
     * @param context
     */
    public static void deletePostAt(int index, Context context) {

        userPosts.remove(index);
        store(userPosts, context);
    }

    /**
     * Stores posts in memory
     * Responsibility of caller to pass correct posts
     *
     * @param userPosts
     * @param context
     */
    public static void store(ArrayList<Post> userPosts, Context context){
        //Store Posts to POSTSAVE file

        try{
            //Store Posts to POSTSAVE file
            FileOutputStream fos = context.openFileOutput(POSTSAVE, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
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

    /**
     * Reads stored posts
     *
     * @param context
     * @return true if Posts are read
     */
    public static boolean tryReadPosts(Context context) {
        try {
            FileInputStream fis = context.openFileInput(POSTSAVE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            userPosts = gson.fromJson(in, new TypeToken<ArrayList<Post>>(){}.getType());

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
