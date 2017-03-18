package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    public ArrayList<Follow> getFollowersWithPosts(Profile followee, int from)
            throws TimeoutException {

        SearchFilter filter = new SearchFilter()
                .addFieldValue(new FieldValue("followeeId", followee.getId()))
                .addNonEmptyField("follower.posts");

        this.elasticSearch.setFilter(filter);

        ArrayList<Follow> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    public ArrayList<Follow> getFollowers(Profile followee, int from) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followeeId",
                followee.getId()));

        this.elasticSearch.setFilter(filter);

        ArrayList<Follow> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    public ArrayList<Follow> getFolloweesWithPosts(Profile follower, int from)
            throws TimeoutException {

        SearchFilter filter = new SearchFilter()
                .addFieldValue(new FieldValue("followerId", follower.getId()))
                .addNonEmptyField("followee.posts");

        this.elasticSearch.setFilter(filter);

        ArrayList<Follow> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    public ArrayList<Follow> getFollowees(Profile follower, int from) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followerId",
                follower.getId()));

        this.elasticSearch.setFilter(filter);

        ArrayList<Follow> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    public Follow getFollowFromID(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    public ArrayList<Follow> getFollows(SearchFilter filter, int from) throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<Follow> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    public void addOrUpdateFollows(Follow... follows) {

        this.elasticSearch.addOrUpdate(follows);
    }

    public void deleteFollows(Follow... follows) {

        this.elasticSearch.delete(follows);
    }
}
