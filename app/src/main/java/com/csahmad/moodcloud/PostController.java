package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

// TODO: 2017-03-18 Sort by date
// TODO: 2017-03-18 Don't request more objects than are used

/**
 * Get {@link Post}s from elastic search or add/update {@link Post}s using elasticsearch.
 *
 * @see ElasticSearch
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
    public ArrayList<Post> getFolloweePosts(Profile follower,
                                            SearchFilter filter, int from) throws TimeoutException {

        ProfileController controller = new ProfileController();

        return this.getLatestPosts(controller.getFolloweesWithPosts(follower, from), filter);
    }

    // Note: latest posts only
    public ArrayList<Post> getFollowerPosts(Profile followee, SearchFilter filter,
                                                   int from) throws TimeoutException {

        ProfileController controller = new ProfileController();

        return this.getLatestPosts(controller.getFollowersWithPosts(followee, from), filter);
    }

    public ArrayList<Post> getPosts(Profile profile, SearchFilter filter, int from)
        throws TimeoutException{

        if (filter == null)
            filter = new SearchFilter();

        this.elasticSearch.setFilter(filter);
        filter.addFieldValue(new FieldValue("posterId", profile.getId()));

        ArrayList<Post> result = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return result;
    }

    public ArrayList<Post> getLatestPosts(ArrayList<Profile> profiles, SearchFilter filter)
        throws TimeoutException{

        ArrayList<Post> latestPosts = new ArrayList<Post>();
        Post post;

        for (Profile profile: profiles) {
            post = this.getLatestPost(profile, filter);
            if (post != null) latestPosts.add(post);
        }

        return latestPosts;
    }

    public Post getLatestPost(Profile profile, SearchFilter filter) throws TimeoutException {

        if (filter == null)
            filter = new SearchFilter();

        this.elasticSearch.setFilter(filter);
        filter.addFieldValue(new FieldValue("posterId", profile.getId()));

        ArrayList<Post> result = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return result.get(0);
    }

    public void deletePosts(Post... posts) {

        this.elasticSearch.delete(posts);
    }
}