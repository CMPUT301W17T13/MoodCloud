package com.csahmad.moodcloud;

import io.searchbox.annotations.JestId;

/**
 * An object that works with elasticsearch methods in the controller classes.
 *
 * @see ElasticSearch
 */
public abstract class ElasticSearchObject {

    /**
     * Whether this is a dummy object.
     *
     * <p>
     * If this is true, certain elasticsearch-related methods (like
     * {@link ElasticSearch#addOrUpdate(ElasticSearchObject[])}) would ignore this object.
     *
     * <p>
     * This is used for situations like {@link Post}s with missing profiles ({@link Post}s with
     * {@link Post#posterId}s that do not correspond to any existing profile) instead of letting
     * the app crash.
     *
     * @see ElasticSearch#addOrUpdate(ElasticSearchObject[])
     * @see ElasticSearch#delete(ElasticSearchObject[])
     * @see ElasticSearchController.AddItems
     * @see ElasticSearchController.DeleteItems
     * @see Post#posterId
     */
    private boolean isDummy = false;
    public static final String dummyText = "[DUMMY]";

    public abstract String getTypeName();

    /** This object's unique ID (creating IDs handled by Jest). */
    @JestId
    protected String id;

    public boolean equals(Object other) {

        if (!(other instanceof ElasticSearchObject)) return false;
        ElasticSearchObject otherElasticSearch = (ElasticSearchObject) other;
        if (!this.getTypeName().equals(otherElasticSearch.getTypeName())) return false;
        if (this.id == null) return this == otherElasticSearch;
        if (otherElasticSearch.getId() == null) return this == otherElasticSearch;
        return this.id.equals(otherElasticSearch.id);
    }

    public boolean isDummy() {

        return this.isDummy;
    }

    public ElasticSearchObject setIsDummy(boolean isDummy) {

        this.isDummy = isDummy;
        return this;
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }
}
