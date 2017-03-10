package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.TimeUnit;

/**
 * Created by oahmad on 2017-03-09.
 */

public class ElasticSearchTest  extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public ElasticSearchTest() {

        super(MainActivity.class);
    }

    public void testConstructor() {

        new ElasticSearch<TestElasticSearchObject>(TestElasticSearchObject.class,
                TestElasticSearchObject.typeName);

        new ElasticSearch<Account>(Account.class, Account.typeName);
        new ElasticSearch<Profile>(Profile.class, Profile.typeName);

        boolean exceptionThrown = false;

        try {
            new ElasticSearch<Account>(null, Account.typeName);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            new ElasticSearch<Account>(Account.class, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            new ElasticSearch<Account>(null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    public void testAddOrUpdate() throws Exception {

        ElasticSearch<TestElasticSearchObject> elasticSearch = ElasticSearchTest.getElasticSearch();

        elasticSearch.addOrUpdate();
        //elasticSearch.waitForTask();
        TimeUnit.SECONDS.sleep(3);

        ;
    }

    private static ElasticSearch<TestElasticSearchObject> getElasticSearch() {

        ElasticSearch<TestElasticSearchObject> elasticSearch =
                new ElasticSearch<TestElasticSearchObject>(TestElasticSearchObject.class,
                        TestElasticSearchObject.typeName);

        elasticSearch.setTimeout(ElasticSearchTest.timeout);

        return elasticSearch;
    }
}