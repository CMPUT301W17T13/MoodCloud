package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// TODO: 2017-03-08 Handle exceptions better

public class ElasticSearch<T extends ElasticSearchObject> {

    private Class type;
    private String typeName;

    private SearchFilter filter;

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
    }

    public boolean itemExists(T object) {

        return this.getById(object.getId()) != null;
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

    public ArrayList<T> getNext(int from) {

        ElasticSearchController.GetItems<T> controller = new ElasticSearchController.GetItems<T>();

        controller.setFrom(from);
        controller.setType(this.type);
        controller.setTypeName(this.typeName);

        controller.execute(this.filter);

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

    // Update if .id not null (otherwise add)
    public void addOrUpdate(T... objects) {

        ElasticSearchController.AddItems<T> controller = new ElasticSearchController.AddItems<T>();
        controller.execute(objects);
    }

    public void delete(T... objects) {

        ElasticSearchController.DeleteItems<T> controller =
                new ElasticSearchController.DeleteItems<T>();

        controller.execute(objects);
    }
}
