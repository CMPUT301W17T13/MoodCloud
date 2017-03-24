package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Get {@link Profile}s from elastic search or add/update {@link Profile}s using elasticsearch.
 *
 * @see ElasticSearch
 */
public class ProfileController {

    private ElasticSearch<Profile> elasticSearch =
            new ElasticSearch<Profile>(Profile.class, Profile.typeName);

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    public ArrayList<Profile> getFollowers(Profile followee, int from) throws TimeoutException {

        ArrayList<Profile> followers = new ArrayList<Profile>();
        FollowController controller = new FollowController();
        ArrayList<Follow> follows = controller.getFollowers(followee, from);
        for (Follow follow: follows) followers.add(follow.getFollower());
        return followers;
    }

    public ArrayList<Profile> getFollowees(Profile follower, int from) throws TimeoutException {

        ArrayList<Profile> followees = new ArrayList<Profile>();
        FollowController controller = new FollowController();
        ArrayList<Follow> follows = controller.getFollowees(follower, from);
        for (Follow follow: follows) followees.add(follow.getFollowee());
        return followees;
    }

    public Profile getProfileFromID(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    public ArrayList<Profile> getProfiles(SearchFilter filter, int from) throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<Profile> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    public void addOrUpdateProfiles(Profile... profiles) {

        this.elasticSearch.addOrUpdate(profiles);
    }

    public void deleteProfiles(Profile... profiles) {

        this.elasticSearch.delete(profiles);
    }
}