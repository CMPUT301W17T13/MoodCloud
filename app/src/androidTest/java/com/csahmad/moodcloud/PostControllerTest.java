package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// TODO: 2017-03-18 Fix profile IDs

/**
 * Created by oahmad on 2017-03-11.
 */

public class PostControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 10_000;

    public PostControllerTest() throws Exception {

        super(MainActivity.class);
    }

    public void testGetPosts() throws Exception {

        PostController controller = PostControllerTest.getController();

        double[] location = {0.0d, 0.0d, 0.0d};

        for (int i = 0; i < 30; i++) {

            controller.addOrUpdatePosts(new Post(    // 0
                    "Asgard's always been my home, but I'm of different blood.",
                    Mood.ANGRY,                                // Mood
                    "Thor",                                 // Trigger text
                    null,                                   // Trigger image
                    SocialContext.ALONE,                                // Social context
                    "fdsfsdf",                    // Poster ID
                    location,                               // Location
                    new GregorianCalendar(900, 2, 14)));
        }

        ArrayList<Post> posts = controller.getPosts(null, 0);

        ;
    }

    public void testGetFolloweePostsWithFilter() throws Exception {

        PostController controller = PostControllerTest.getController();
        ProfileController profileController = new ProfileController();
        profileController.setTimeout(PostControllerTest.timeout);
        FollowController followController = new FollowController();
        followController.setTimeout(PostControllerTest.timeout);

        Profile follower = new Profile("Jane the Follower");
        profileController.addOrUpdateProfiles(follower);
        profileController.waitForTask();

        Profile followee1 = new Profile("Jill the Followee");
        profileController.addOrUpdateProfiles(followee1);
        profileController.waitForTask();

        Follow follow = new Follow(follower, followee1);
        followController.addOrUpdateFollows(follow);
        followController.waitForTask();

        double[] location = {0.0d, 0.0d, 0.0d};

        Post followee1post1 = new Post(    // 0
                "I am angry",
                Mood.ANGRY,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                followee1.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(900, 2, 14));

        Post followee1post2 = new Post(    // 0
                "I am sad",
                Mood.SAD,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                followee1.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(900, 2, 14));

        Post followee1post3 = new Post(    // 0
                "I am angry again",
                Mood.ANGRY,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                followee1.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(900, 2, 15));

        Post followee1post4 = new Post(    // 0
                "I am confused",
                Mood.CONFUSED,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                followee1.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(901, 2, 15));

        controller.addOrUpdatePosts(followee1post1, followee1post2, followee1post3, followee1post4);
        controller.waitForTask();

        Post followerPost = new Post(    // 0
                "I am a follower post",
                Mood.ANGRY,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                follower.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(3000, 2, 15));

        controller.addOrUpdatePosts(followerPost);
        controller.waitForTask();

        SearchFilter filter = new SearchFilter();

        ArrayList<Post> expected = new ArrayList<Post>();
        expected.add(followee1post4);

        ArrayList<Post> results = controller.getFolloweePosts(follower, filter, 0);
        assertEquals(expected, results);

        filter.setMood(Mood.ANGRY);
        expected.clear();
        expected.add(followee1post3);

        results = controller.getFolloweePosts(follower, filter, 0);
        assertEquals(expected, results);

        filter.sortByDate();

        results = controller.getFolloweePosts(follower, filter, 0);
        assertEquals(expected, results);

        Profile followee2 = new Profile("Jack the Followee");
        profileController.addOrUpdateProfiles(followee2);
        profileController.waitForTask();

        Follow follow2 = new Follow(follower, followee2);
        followController.addOrUpdateFollows(follow2);
        followController.waitForTask();

        Post followee2post1 = new Post(    // 0
                "I too am angry",
                Mood.ANGRY,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                followee2.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(2015, 2, 15));

        controller.addOrUpdatePosts(followee2post1);
        controller.waitForTask();

        expected.clear();
        expected.add(followee1post3);
        expected.add(followee2post1);

        results = controller.getFolloweePosts(follower, filter, 0);
        assertEquals(expected, results);

        Calendar currentDate = GregorianCalendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        Calendar twoWeeksAgo = GregorianCalendar.getInstance();
        twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);
        int twoWeeksAgoYear = twoWeeksAgo.get(Calendar.YEAR);
        int twoWeeksAgoMonth = twoWeeksAgo.get(Calendar.MONTH);
        int twoWeeksAgoDay = twoWeeksAgo.get(Calendar.DAY_OF_MONTH);

        Post followee1post5 = new Post(    // 0
                "Within week",
                Mood.CONFUSED,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                followee1.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(currentYear, currentMonth, currentDay));

        Post followee1post6 = new Post(    // 0
                "Not within week",
                Mood.CONFUSED,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                followee1.getId(),                    // Poster ID
                location,                               // Location
                new GregorianCalendar(twoWeeksAgoYear, twoWeeksAgoMonth, twoWeeksAgoDay));

        controller.addOrUpdatePosts(followee1post5, followee1post6);
        controller.waitForTask();

        filter = new SearchFilter().setMaxTimeUnitsAgo(1);

        expected.clear();
        expected.add(followee1post5);

        results = controller.getFolloweePosts(follower, filter, 0);
        assertEquals(expected, results);

        filter = new SearchFilter()
                .setLocation(new SimpleLocation(0.0d, 0.0d, 0.0d))
                .setMaxDistance(1.1d);

        results = controller.getFolloweePosts(follower, filter, 0);

        ;

        profileController.deleteProfiles(follower, followee1, followee2);
        followController.deleteFollows(follow, follow2);
        controller.deletePosts(followerPost, followee1post1, followee1post2, followee1post3,
                followee1post4, followee1post5, followee1post6, followee2post1);
    }

    public void testGetFolloweePostsNoFilter() throws Exception {

        ;
    }

    ;

    private static PostController getController() throws Exception {

        PostController controller = new PostController();
        controller.setTimeout(PostControllerTest.timeout);
        return controller;
    }
}
