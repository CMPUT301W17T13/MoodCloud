package com.csahmad.moodcloud;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

    /**
     * Return the number of occurrences of each mood for all posts matching the restrictions in the
     * given {@link SearchFilter}.
     *
     * <p>
     * If filter is null, return the number of occurrences of each mood for all posts.
     *
     * @param filter restrictions for which posts to count
     * @return the number of occurrences of each mood
     * @see Post#getMood()
     * @see Post#setMood(int)
     */
    public HashMap<Integer, Long> getMoodCounts(SearchFilter filter) throws TimeoutException {

        int[] moods = {Mood.ANGRY, Mood.CONFUSED, Mood.DISGUSTED, Mood.SCARED, Mood.HAPPY,
            Mood.SAD, Mood.ASHAMED, Mood.SURPRISED};

        if (filter == null)
            filter = new SearchFilter();

        filter.addTermAggregation("mood");
        this.elasticSearch.setFilter(filter);

        HashMap<String, Long> countsStringKeys = this.elasticSearch.getTermCounts().get("mood");
        HashMap<Integer, Long> counts = new HashMap<Integer, Long>();

        for (String stringKey: countsStringKeys.keySet())
            counts.put(Integer.parseInt(stringKey), countsStringKeys.get(stringKey));

        for (int mood: moods) {

            if (!counts.containsKey(mood))
                counts.put(mood, 0l);
        }

        this.elasticSearch.setFilter(null);
        filter.removeTermAggregation("mood");

        return counts;
    }

    /**
     * Add or update the given {@link Post}s via elasticsearch.
     *
     * <p>
     * If a {@link Post} has a null ID, add it. If a {@link Post} has a non-null
     * ID, update it.
     *
     * @param posts the {@link Post}s to add or update
     */
    public void addOrUpdatePosts(Post... posts) {

        this.elasticSearch.addOrUpdate(posts);
    }

    /**
     * Wait for the last task to finish executing.
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    /**
     * Return the {@link Post} that has the given ID.
     *
     * <p>
     * Return null if no {@link Post} has the given ID.
     *
     * @param id the ID of the desired {@link Post}
     * @return the {@link Post} that has the given ID
     * @throws TimeoutException
     */
    public Post getPostFromId(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    /**
     * Return {@link Post}s that match the given filter.
     *
     * <p>
     * If filter is null or has no restrictions, return all {@link Post}s.
     *
     * @param filter restricts which {@link Post}s will be returned (defines conditions each
     *               {@link Post} must satisfy)
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return {@link Post}s from the elasticsearch index
     * @throws TimeoutException
     */
    public ArrayList<Post> getPosts(SearchFilter filter, int from) throws TimeoutException {

        if (filter == null)
            filter = new SearchFilter();

        filter.sortByDate();

        this.elasticSearch.setFilter(filter);
        ArrayList<Post> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    /**
     * Return the latest {@link Post} of each followee of the given follower.
     *
     * @param follower the follower of the followees with the desired posts
     * @param filter restricts which {@link Post}s will be returned (defines conditions each
     *               {@link Post} must satisfy)
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return the latest {@link Post} of each followee of the given follower
     * @throws TimeoutException
     */
    public ArrayList<Post> getFolloweePosts(Profile follower,
                                            SearchFilter filter, int from) throws TimeoutException {

        ProfileController controller = new ProfileController();
        return this.getLatestPosts(controller.getFollowees(follower, from), filter);
    }

    /**
     * Return the latest {@link Post} of each follower of the given followee.
     *
     * @param followee the followee of the followers with the desired posts
     * @param filter restricts which {@link Post}s will be returned (defines conditions each
     *               {@link Post} must satisfy)
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return the latest {@link Post} of each follower of the given followee
     * @throws TimeoutException
     */
    public ArrayList<Post> getFollowerPosts(Profile followee, SearchFilter filter,
                                                   int from) throws TimeoutException {

        ProfileController controller = new ProfileController();
        return this.getLatestPosts(controller.getFollowers(followee, from), filter);
    }

    /**
     * Return the {@link Post}s associated with the given profile.
     *
     * @param profile the poster of the posts to return
     * @param filter restricts which {@link Post}s will be returned (defines conditions each
     *               {@link Post} must satisfy)
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return the {@link Post}s posted by the given profile
     * @throws TimeoutException
     */
    public ArrayList<Post> getPosts(Profile profile, SearchFilter filter, int from)
        throws TimeoutException{

        if (filter == null)
            filter = new SearchFilter();

        this.elasticSearch.setFilter(filter);

        filter.addFieldValue(new FieldValue("posterId", profile.getId()))
                .sortByDate();

        ArrayList<Post> result = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return result;
    }

    /**
     * Return the latest {@link Post} of each of the given profiles.
     *
     * @param profiles the {@link Profile}s with the posts to return
     * @param filter restricts which {@link Post}s will be returned (defines conditions each
     *               {@link Post} must satisfy)
     * @return the latest {@link Post} posted by each profile
     * @throws TimeoutException
     */
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

    /**
     * Return the latest {@link Post} posted by the given profile.
     *
     * <p>
     * If there are no posts to return, return null.
     *
     * @param profile the poster of the post to return
     * @param filter restricts which {@link Post}s will be returned (defines conditions each
     *               {@link Post} must satisfy)
     * @return the latest {@link Post} posted by the given profile
     * @throws TimeoutException
     */
    public Post getLatestPost(Profile profile, SearchFilter filter) throws TimeoutException {

        if (filter == null)
            filter = new SearchFilter();

        this.elasticSearch.setFilter(filter);
        filter.removeFieldValue("posterId");
        filter.addFieldValue(new FieldValue("posterId", profile.getId()))
                .sortByDate();

        Post result = this.elasticSearch.getSingleResult();
        this.elasticSearch.setFilter(null);
        return result;
    }

    /**
     * Delete the given {@link Post}s via elasticsearch.
     *
     * @param posts
     */
    public void deletePosts(Post... posts) {

        this.elasticSearch.delete(posts);
    }
}