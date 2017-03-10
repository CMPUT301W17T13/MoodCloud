package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-01.
 */

public class PostController {

    private static ElasticSearch<Post> elasticSearch =
            new ElasticSearch<Post>(Post.class, Post.typeName);

    // Note: stores post in LocoalData.homeProfile AND sends the post to the internets
    public void addOrUpdatePosts(Post... posts) {

        PostController.elasticSearch.addOrUpdate(posts);
    }

    public Post getPostFromId(String id) throws TimeoutException {

        return PostController.elasticSearch.getById(id);
    }

    public ArrayList<Post> getPosts(SearchFilter filter, int from) throws TimeoutException {

        PostController.elasticSearch.setFilter(filter);
        ArrayList<Post> result = PostController.elasticSearch.getNext(from);
        PostController.elasticSearch.setFilter(null);
        return result;
    }

    public void deletePosts(Post... posts) {

        PostController.elasticSearch.delete(posts);
    }
}