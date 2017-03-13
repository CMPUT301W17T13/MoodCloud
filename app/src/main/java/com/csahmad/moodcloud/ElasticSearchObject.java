package com.csahmad.moodcloud;

import io.searchbox.annotations.JestId;

/**
 * Created by oahmad on 2017-03-06.
 */

public abstract class ElasticSearchObject {

    public abstract String getTypeName();

    /** This object's unique ID (creating IDs handled by Jest). */
    @JestId
    protected String id;

    public boolean equals(Object other) {

        if (!(other instanceof ElasticSearchObject)) return false;
        ElasticSearchObject otherElasticSearch = (ElasticSearchObject) other;
        if (!this.getTypeName().equals(otherElasticSearch.getTypeName())) return false;
        if (this.id == null) return this == otherElasticSearch;
        return this.id.equals(otherElasticSearch.id);
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }
}
