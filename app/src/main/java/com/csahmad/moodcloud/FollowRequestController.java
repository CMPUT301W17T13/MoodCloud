package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Get {@link FollowRequest}s from elastic search or add/update {@link FollowRequest}s using
 * elasticsearch.
 *
 * @see ElasticSearch
 */
public class FollowRequestController {

    private ElasticSearch<FollowRequest> elasticSearch =
            new ElasticSearch<FollowRequest>(FollowRequest.class, FollowRequest.typeName);

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    public boolean requestExists(Profile follower, Profile followee) {

        try {

            SearchFilter filter = new SearchFilter()
                    .addFieldValue(new FieldValue("followerId", follower.getId()))
                    .addFieldValue(new FieldValue("followeeId", followee.getId()));

            this.elasticSearch.setFilter(filter);

            ArrayList<FollowRequest> results = this.getFollowRequests(filter, 0);

            if (results.size() > 0) return true;
            return false;
        }

        catch (TimeoutException e) {}
        return false;
    }

    public ArrayList<FollowRequest> getFollowRequests(Profile followee, int from)
            throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followeeId",
                followee.getId()));

        this.elasticSearch.setFilter(filter);

        ArrayList<FollowRequest> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    public FollowRequest getFollowRequestFromID(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    public ArrayList<FollowRequest> getFollowRequests(SearchFilter filter, int from)
            throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<FollowRequest> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    public void addOrUpdateFollows(FollowRequest... followRequests) {

        this.elasticSearch.addOrUpdate(followRequests);
    }

    public void deleteFollowRequests(FollowRequest... followRequests) {

        this.elasticSearch.delete(followRequests);
    }
}
