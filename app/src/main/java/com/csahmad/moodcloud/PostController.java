package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-01.
 */

public class PostController {

    private ElasticSearch<Post> elasticSearch =
            new ElasticSearch<Post>(Post.class, Post.typeName);

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    // Note: stores post in LocoalData.homeProfile AND sends the post to the internets
    public void addOrUpdatePosts(Post... posts) {

        this.elasticSearch.addOrUpdate(posts);
    }

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    public Post getPostFromId(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    public ArrayList<Post> getPosts(SearchFilter filter, int from) throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<Post> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    // Note: latest posts only
    public ArrayList<Post> getFolloweePosts(Profile follower, SearchFilter filter, int from) {

        return new ArrayList<Post>();
    }

    // Note: latest posts only
    public ArrayList<Post> getFollowerPosts(Profile followee, SearchFilter filter, int from) {

        ArrayList<Profile> followers = followee.getFollowers();
        ArrayList<Post> posts = new ArrayList<Post>();

        for (Profile follower: followers)
            posts.add(PostController.getLatestPost(posts, filter));

        return posts;
    }

    // TODO: 2017-03-11 Ignores filter
    public static Post getLatestPost(ArrayList<Post> posts, SearchFilter filter) {

        if (posts == null)
            throw new IllegalArgumentException("Cannot pass null.");

        if (posts.size() == 0)
            return null;

        Post post;
        Calendar postDate;

        Post latestPost = posts.get(0);
        Calendar latestPostDate;

        for (int i = 0; i < posts.size(); i++) {

            post = posts.get(i);
            postDate = post.getDate();
            latestPostDate = latestPost.getDate();

            if (postDate.compareTo(latestPostDate) < 0)
                latestPost = post;
        }

        return latestPost;
    }

    public void deletePosts(Post... posts) {

        this.elasticSearch.delete(posts);
    }
}