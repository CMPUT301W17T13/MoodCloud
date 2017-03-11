package com.csahmad.moodcloud;

import java.util.ArrayList;
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

    public ArrayList<Post> getFolloweePosts(Profile follower, SearchFilter filter, int from) {

        return new ArrayList<Post>();
    }

    public ArrayList<Post> getFollowerPosts(Profile followee, SearchFilter filter, int from) {

        return new ArrayList<Post>();
    }

    public void deletePosts(Post... posts) {

        this.elasticSearch.delete(posts);
    }
}