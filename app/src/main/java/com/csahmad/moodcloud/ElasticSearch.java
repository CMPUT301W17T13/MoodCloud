package com.csahmad.moodcloud;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// TODO: 2017-03-08 Handle exceptions better

public class ElasticSearch<T extends ElasticSearchObject> {

    private Class type;
    private String typeName;

    private SearchFilter filter;

    private AsyncTask lastExecute;

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

    /** Wait for the last AsyncTask execution to finish. */
    public void waitForTask() throws ExecutionException, InterruptedException {

        this.lastExecute.get();
    }

    /** Wait for the last AsyncTask execution to finish for the given amount of time. */
    public void waitForTask(int milliseconds)
            throws ExecutionException, InterruptedException, TimeoutException {

        this.lastExecute.get(milliseconds, TimeUnit.MILLISECONDS);
    }

    public T getById(String id) {

        ElasticSearchController.GetById<T> controller = new ElasticSearchController.GetById<T>();

        controller.setType(this.type);
        controller.setTypeName(this.typeName);

        this.lastExecute = controller.execute(id);

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

        this.lastExecute = controller.execute(this.filter);

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
        this.lastExecute = controller.execute(objects);
    }

    public void delete(T... objects) {

        ElasticSearchController.DeleteItems<T> controller =
                new ElasticSearchController.DeleteItems<T>();

        this.lastExecute = controller.execute(objects);
    }
}
