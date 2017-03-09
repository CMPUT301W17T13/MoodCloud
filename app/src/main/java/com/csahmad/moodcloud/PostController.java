package com.csahmad.moodcloud;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-01.
 */

public class PostController {

    // Note: stores post in LocoalData.homeProfile AND sends the post to the internets
    public void addOrUpdatePosts(Post... posts) {

        PostController.getElasticSearch().addOrUpdate(posts);
    }

    public Post getPostFromId(String id) {

        return PostController.getElasticSearch().getById(id);
    }

    public ArrayList<Post> getPosts(SearchFilter filter, int from) {

        ElasticSearch<Post> elasticSearch = PostController.getElasticSearch();
        elasticSearch.setFilter(filter);
        return elasticSearch.getNext(from);
    }

    public void deletePosts(Post... posts) {

        PostController.getElasticSearch().delete(posts);
    }

    public static ElasticSearch<Post> getElasticSearch() {

        return new ElasticSearch<Post>(Post.class, Post.typeName);
    }
}