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
     * Return the ID of every person the given {@link Profile} follows.
     *
     * @param profile the follower
     * @return the ID of every person the given {@link Profile} follows
     * @see Follow
     */
    public ArrayList<String> getAllFolloweeIds(Profile profile) throws TimeoutException {

        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<Profile> profiles;
        int from = 0;

        do {

            profiles = this.getFollowees(profile, from);
            for (Profile p: profiles) ids.add(p.getId());
            from += ElasticSearchController.getResultSize() - 1;

        } while (profiles.size() >= ElasticSearchController.getResultSize());

        return ids;
    }

    public Profile getProfileFromUsername(String username) throws TimeoutException {

        AccountController controller = new AccountController();
        Account account = controller.getAccountFromUsername(username);

        if (account == null) return null;
        return account.getProfile();
    }

    /**
     * Get the followees of the given {@link Profile}.
     *
     * @param follower the {@link Profile} to return the followees of
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return the followees of the given {@link Profile}
     * @throws TimeoutException
     */
    public ArrayList<Profile> getFollowees(Profile follower, int from) throws TimeoutException {

        ArrayList<Profile> followees = new ArrayList<Profile>();
        FollowController controller = new FollowController();
        ArrayList<Follow> follows = controller.getFollowees(follower, from);
        for (Follow follow: follows) followees.add(follow.getFollowee());
        return followees;
    }

    /**
     * Return the {@link Profile} that has the given ID.
     *
     * <p>
     * Return null if no {@link Profile} has the given ID.
     *
     * @param id the ID of the desired {@link Profile}
     * @return the {@link Profile} that has the given ID
     * @throws TimeoutException
     */
    public Profile getProfileFromID(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    /**
     * Return {@link Profile}s that match the given filter.
     *
     * <p>
     * If filter is null or has no restrictions, return all {@link Profile}s.
     *
     * @param filter restricts which {@link Profile}s will be returned (defines conditions each
     *               {@link Profile} must satisfy)
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return {@link Profile}s from the elasticsearch index
     * @throws TimeoutException
     */
    public ArrayList<Profile> getProfiles(SearchFilter filter, int from) throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<Profile> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    /**
     * Add or update the given {@link Profile}s via elasticsearch.
     *
     * <p>
     * If a {@link Profile} has a null ID, add it. If a {@link Profile} has a
     * non-null ID, update it.
     *
     * @param profiles the {@link Profile}s to add or update
     */
    public void addOrUpdateProfiles(Profile... profiles) {

        this.elasticSearch.addOrUpdate(profiles);
    }

    /**
     * Delete the given {@link Profile}s via elasticsearch.
     *
     * @param profiles
     */
    public void deleteProfiles(Profile... profiles) {

        this.elasticSearch.delete(profiles);
    }
}