package com.csahmad.moodcloud;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-01.
 */

public class ProfileController {

    public Profile getProfileFromID(String id) {

        return ProfileController.getElasticSearch().getById(id);
    }

    public ArrayList<Profile> getProfiles(SearchFilter filter, int from) {

        ElasticSearch<Profile> elasticSearch = ProfileController.getElasticSearch();
        elasticSearch.setFilter(filter);
        return elasticSearch.getNext(from);
    }

    public void addOrUpdateProfiles(Profile... profiles) {

        ProfileController.getElasticSearch().addOrUpdate(profiles);

    }

    public void deleteProfiles(Profile... profiles) {

        ProfileController.getElasticSearch().delete(profiles);
    }

    public static ElasticSearch<Profile> getElasticSearch() {

        return new ElasticSearch<Profile>(Profile.class, Profile.typeName);
    }
}