package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Get {@link Follow} objects from elastic search or add/update {@link Follow} objects using
 * elasticsearch.
 *
 * @see ElasticSearch
 */
public class FollowController {

    private ElasticSearch<Follow> elasticSearch =
            new ElasticSearch<Follow>(Follow.class, Follow.typeName);

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
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
     * Return whether the given {@link Follow} relationship exists.
     *
     * @param follower the follower in the {@link Follow} relationship
     * @param followee the followee in the {@link Follow} relationship
     * @return
     */
    public boolean followExists(Profile follower, Profile followee) {
        try {
            ArrayList<Follow> followers = getFollowers(followee, 0);
            for (int i=0; i<followers.size(); i++){
                if (follower.equals(followers.get(i).getFollower())){
                    return TRUE;
                }
            }
            return FALSE;
        } catch (TimeoutException e){}
        return FALSE;
    }

    /**
     * Return each {@link Follow} relationship where the given followee is a followee in the
     * relationship.
     *
     * @param followee the followee in the {@link Follow} relationships
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return each {@link Follow} relationship where the given profile is a followee
     * @throws TimeoutException
     */
    public ArrayList<Follow> getFollowers(Profile followee, int from) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followeeId",
                followee.getId()));

        this.elasticSearch.setFilter(filter);

        ArrayList<Follow> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    /**
     * Return each {@link Follow} relationship where the given follower is a follower in the
     * relationship.
     *
     * @param follower the follower in the {@link Follow} relationships
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return each {@link Follow} relationship where the given profile is a follower
     * @throws TimeoutException
     */
    public ArrayList<Follow> getFollowees(Profile follower, int from) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followerId",
                follower.getId()));

        this.elasticSearch.setFilter(filter);

        ArrayList<Follow> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    /**
     * Return the {@link Follow} that has the given id.
     *
     * <p>
     * Return null if no {@link Follow} has the given id.
     *
     * @param id the id of the desired {@link Follow}
     * @return the {@link Follow} that has the given id
     * @throws TimeoutException
     */
    public Follow getFollowFromID(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    /**
     * Return {@link Follow}s that match the given filter.
     *
     * <p>
     * If filter is null or has no restrictions, return all {@link Follow}s.
     *
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return {@link Follow}s from the elasticsearch index
     * @throws TimeoutException
     */
    public ArrayList<Follow> getFollows(SearchFilter filter, int from) throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<Follow> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    /**
     * Add or update the given {@link Follow}s via elasticsearch.
     *
     * <p>
     * If a {@link Follow} has a null {@link Follow#id}, add it. If a {@link Follow} has a
     * non-null {@link Follow#id}, update it.
     *
     * @param follows the {@link Follow}s to add or update
     */
    public void addOrUpdateFollows(Follow... follows) {

        this.elasticSearch.addOrUpdate(follows);
    }

    /**
     * Delete the given {@link Follow}s via elasticsearch.
     *
     * @param follows
     */
    public void deleteFollows(Follow... follows) {

        this.elasticSearch.delete(follows);
    }
}
