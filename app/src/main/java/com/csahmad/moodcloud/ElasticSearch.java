package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// TODO: 2017-03-08 Handle exceptions better
// TODO: 2017-03-08 Update item
// TODO: 2017-03-08 Delete items

public class ElasticSearch<T extends ElasticSearchObject> {

    private Class type;
    private String typeName;

    private SearchFilter filter;
    private int from = 0;

    public ElasticSearch(Class type, String typeName) {

        if (type == null || typeName == null)
            throw new IllegalArgumentException("Cannot pass null arguments.");

        this.type = type;
        this.typeName = typeName;
    }

    public Class getType() {

        return this.type;
    }

    public String getTypeName() {

        return this.typeName;
    }

    public SearchFilter getFilter() {

        return this.filter;
    }

    public void setFilter(SearchFilter filter) {

        this.filter = filter;
        this.from = 0;
    }

    public T getById(String id) {

        ElasticSearchController.GetById<T> controller = new ElasticSearchController.GetById<T>();

        controller.setType(this.type);
        controller.setTypeName(this.typeName);

        controller.execute(id);

        try {
            return controller.get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }

        catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void advanceFrom() {

        this.from += ElasticSearchController.getResultSize();
    }

    public ArrayList<T> getNext() {

        ElasticSearchController.GetItems<T> controller = new ElasticSearchController.GetItems<T>();

        controller.setFrom(this.from);
        controller.setType(this.type);
        controller.setTypeName(this.typeName);

        controller.execute(this.filter);
        this.advanceFrom();

        try {
            return controller.get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }

        catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<T> getFromStart() {

        this.from = 0;
        return this.getNext();
    }

    public void add(T... objects) {

        ElasticSearchController.AddItems<T> controller = new ElasticSearchController.AddItems<T>();
        controller.execute(objects);
    }
}
