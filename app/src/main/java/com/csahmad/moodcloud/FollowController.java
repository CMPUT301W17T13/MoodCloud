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

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

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

    public ArrayList<Follow> getFollowers(Profile followee, int from) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followeeId",
                followee.getId()));

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
