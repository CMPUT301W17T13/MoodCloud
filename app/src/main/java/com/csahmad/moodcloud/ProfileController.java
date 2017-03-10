package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-01.
 */

public class ProfileController {

    private static ElasticSearch<Profile> elasticSearch =
            new ElasticSearch<Profile>(ProfileController.class, Profile.typeName);

    public Profile getProfileFromID(String id) throws TimeoutException {

        return ProfileController.elasticSearch.getById(id);
    }

    public ArrayList<Profile> getProfiles(SearchFilter filter, int from) throws TimeoutException {

        ProfileController.elasticSearch.setFilter(filter);
        ArrayList<Profile> result = ProfileController.elasticSearch.getNext(from);
        ProfileController.elasticSearch.setFilter(null);
        return null;
    }

    public void addOrUpdateProfiles(Profile... profiles) {

        ProfileController.elasticSearch.addOrUpdate(profiles);

    }

    public void deleteProfiles(Profile... profiles) {

        ProfileController.elasticSearch.delete(profiles);
    }
}