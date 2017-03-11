package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;
import java.util.GregorianCalendar;

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
        elasticSearch.waitForTask();

        TestElasticSearchObject object1 = new TestElasticSearchObject();
        assertNull(object1.getId());

        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();
        assertNotNull(object1.getId());

        String id1 = object1.getId();

        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);

        TestElasticSearchObject object2 = new TestElasticSearchObject();
        assertNull(object2.getId());

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertNotNull(object2.getId());

        String id2 = object2.getId();

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);

        String message = "Don't break my heart, Mickey";
        object1.setMessage(message);

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);

        SimpleLocation location = new SimpleLocation(1.2d, -0.8d, 0.0d);
        object2.setLocation(location);

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);

        message = "We like to party";
        object2.setMessage(message);

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);
    }

    public void testGetById() throws Exception {

        ElasticSearch<TestElasticSearchObject> elasticSearch = ElasticSearchTest.getElasticSearch();

        TestElasticSearchObject returned = elasticSearch.getById("ThisIdDoesNotExist");
        assertNull(returned);

        TestElasticSearchObject object1 = new TestElasticSearchObject();

        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();

        String id = object1.getId();
        returned = elasticSearch.getById(id);
        assertNotNull(returned);
        assertEquals(object1, returned);

        TestElasticSearchObject object2 = new TestElasticSearchObject();
        object2.setMessage(
                "He's my best friend / Best of all best friends / Will you be my best friend too?");

        elasticSearch.addOrUpdate(object2);
        elasticSearch.waitForTask();

        id = object2.getId();
        returned = elasticSearch.getById(id);
        assertNotNull(returned);
        assertEquals(object2, returned);

        TestElasticSearchObject object3 = new TestElasticSearchObject();
        object3.setMessage("Change Leopardon! Yeah yeah yeeeaah, WOW!");
        object3.setMood("Happy");
        object3.setDate(new GregorianCalendar(1994, 8, 26));
        object3.setLocation(new SimpleLocation(2.8d, 32.5d, 0.0d));

        elasticSearch.addOrUpdate(object3);
        elasticSearch.waitForTask();

        id = object3.getId();
        returned = elasticSearch.getById(id);
        assertNotNull(returned);
        assertEquals(object3, returned);
    }

    private static ElasticSearch<TestElasticSearchObject> getElasticSearch() {

        ElasticSearch<TestElasticSearchObject> elasticSearch =
                new ElasticSearch<TestElasticSearchObject>(TestElasticSearchObject.class,
                        TestElasticSearchObject.typeName);

        elasticSearch.setTimeout(ElasticSearchTest.timeout);

        return elasticSearch;
    }
}