package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-12.
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
