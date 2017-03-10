package com.csahmad.moodcloud;

import java.util.ArrayList;
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