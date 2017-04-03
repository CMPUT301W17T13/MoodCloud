package com.csahmad.moodcloud;

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
     * Return the number of occurrences of each mood for all posts by the followees of the given
     * poster matching the restrictions in the given {@link SearchFilter}.
     *
     * <p>
     * If filter is null, return the number of occurrences of each mood for all posts.
     *
     * @param filter restrictions for which posts to count
     * @param follower the follower of the followees to restrict posts to
     * @return the number of occurrences of each mood
     * @see Post#getMood()
     * @see Post#setMood(int)
     */
    public HashMap<Integer, Long> getFolloweeMoodCounts(SearchFilter filter,
                                                Profile follower) throws TimeoutException {

        if (filter == null) filter = new SearchFilter();
        ArrayList<String> followeeIds = new ProfileController().getAllFolloweeIds(follower);
        filter.addFieldValueRange(new FieldValues("posterId", followeeIds));
        return this.getMoodCounts(filter);
    }

    /**
     * Return the number of occurrences of each mood for all posts by the given poster matching the
     * restrictions in the given {@link SearchFilter}.
     *
     * <p>
     * If filter is null, return the number of occurrences of each mood for all posts.
     *
     * @param filter restrictions for which posts to count
     * @param poster the poster to restrict posts to
     * @return the number of occurrences of each mood
     * @see Post#getMood()
     * @see Post#setMood(int)
     */
    public HashMap<Integer, Long> getMoodCounts(SearchFilter filter,
                                                Profile poster) throws TimeoutException {

        if (filter == null) filter = new SearchFilter();
        filter.addFieldValue(new FieldValue("posterId", poster.getId()));
        return this.getMoodCounts(filter);
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
     * Return the {@link Post}s of each followee of the given follower.
     *
     * @param follower the follower of the followees with the desired posts
     * @param filter restricts which {@link Post}s will be returned (defines conditions each
     *               {@link Post} must satisfy)
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return the {@link Post}s of each followee of the given follower
     * @throws TimeoutException
     */
    public ArrayList<Post> getFolloweePosts(Profile follower,
                                            SearchFilter filter, int from) throws TimeoutException {

        ArrayList<String> followeeIds = new ProfileController().getAllFolloweeIds(follower);

        if (filter == null)
            filter = new SearchFilter();

        filter.addFieldValueRange(new FieldValues("posterId", followeeIds))
            .sortByDate();

        return this.getPosts(filter, from);
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
    public ArrayList<Post> getLatestFolloweePosts(Profile follower,
                                                  SearchFilter filter, int from)
            throws TimeoutException {

        ArrayList<String> followeeIds = new ProfileController().getAllFolloweeIds(follower);
        ArrayList<Post> posts = new ArrayList<Post>();

        if (filter == null)
            filter = new SearchFilter();

        filter.sortByDate();
        this.elasticSearch.setFilter(filter);

        for (String id: followeeIds) {
            filter.addFieldValue(new FieldValue("posterId", id));
            posts.add(this.elasticSearch.getSingleResult());
            filter.removeFieldValue("posterId");
        }

        this.elasticSearch.setFilter(null);
        return posts;
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

        ArrayList<Post> result = this.elasticSearch.getNext(from);
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