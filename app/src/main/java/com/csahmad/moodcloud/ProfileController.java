package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-01.
 */

public class ProfileController {

    private ElasticSearch<Profile> elasticSearch =
            new ElasticSearch<Profile>(ProfileController.class, Profile.typeName);

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    // TODO: 2017-03-11 Find a better way?
    // TODO: 2017-03-11 Remove from?
    public ArrayList<Profile> getFollowees(Profile follower, int from) throws TimeoutException {

        ArrayList<Profile> followees = new ArrayList<Profile>();
        ArrayList<Profile> results;

        from = 0;

        do {

            results = this.elasticSearch.getNext(from);
            followees.addAll(ProfileController.getFolloweesFrom(follower, results));
            from += ElasticSearchController.getResultSize();

        } while (results.size() > 0);

        return followees;
    }

    private static ArrayList<Profile> getFolloweesFrom(Profile follower,
                                                       ArrayList<Profile> profiles) {

        ArrayList<Profile> followees = new ArrayList<Profile>();

        for (Profile profile: profiles) {

            if (profile.getFollowers().contains(follower))
                followees.add(profile);
        }

        return followees;
    }

    public Profile getProfileFromID(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    public ArrayList<Profile> getProfiles(SearchFilter filter, int from) throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<Profile> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return null;
    }

    public void addOrUpdateProfiles(Profile... profiles) {

        this.elasticSearch.addOrUpdate(profiles);

    }

    public void deleteProfiles(Profile... profiles) {

        this.elasticSearch.delete(profiles);
    }
}