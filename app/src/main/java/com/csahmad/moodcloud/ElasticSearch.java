package com.csahmad.moodcloud;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// TODO: 2017-03-08 Handle exceptions better

/**
 * Get {@link ElasticSearchObject}s using elasticsearch or add/update {@link ElasticSearchObject}s
 * using elasticsearch.
 *
 * @see ElasticSearchController
 */
public class ElasticSearch<T extends ElasticSearchObject> {

    private Class type;
    private String typeName;

    private SearchFilter filter;

    private AsyncTask lastTask;
    private Integer timeout;

    public ElasticSearch(Class type, String typeName) {

        if (type == null || typeName == null)
            throw new IllegalArgumentException("Cannot pass null arguments.");

        this.type = type;
        this.typeName = typeName;
    }

    public Integer getTimeout() {

        return this.timeout;
    }

    public void setTimeout(Integer timeout) {

        this.timeout = timeout;
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

    /** Wait for the last AsyncTask execution to finish. */
    public void waitForTask()
            throws ExecutionException, InterruptedException, TimeoutException {

        if (this.lastTask == null)
            throw new IllegalStateException("Nothing to wait for.");

        if (this.timeout == null)
            this.lastTask.get();

        else
            this.lastTask.get(this.timeout, TimeUnit.MILLISECONDS);
    }

    public T getById(String id) throws TimeoutException {

        if (id == null)
            throw new IllegalArgumentException("id is null.");

        ElasticSearchController.GetById<T> controller = new ElasticSearchController.GetById<T>();
        this.lastTask = controller;

        controller.setType(this.type);
        controller.setTypeName(this.typeName);

        controller.execute(id);

        try {

            if (this.timeout == null)
                return controller.get();

            else
                return controller.get(this.timeout, TimeUnit.MILLISECONDS);
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }

        catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public T getSingleResult() throws TimeoutException {

        ArrayList<T> results = this.getNext(0, true);
        if (results.size() > 0) return results.get(0);
        return null;
    }

    public ArrayList<T> getNext(int from) throws TimeoutException {

        return this.getNext(from, false);
    }

    private ArrayList<T> getNext(int from, boolean singleResult) throws TimeoutException {

        this.refreshIndex();

        try {
            this.waitForTask();
        }

        catch (ExecutionException e) {
            Log.i("Error", "ExecutionException");
        }

        catch (InterruptedException e) {
            Log.i("Error", "InterruptedException");
        }

        ElasticSearchController.GetItems<T> controller = new ElasticSearchController.GetItems<T>();
        this.lastTask = controller;

        if (singleResult) controller.setSingleResult(true);
        controller.setFrom(from);
        controller.setType(this.type);
        controller.setTypeName(this.typeName);

        controller.execute(this.filter);

        try {

            if (this.timeout == null)
                return controller.get();

            else
                return controller.get(this.timeout, TimeUnit.MILLISECONDS);
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

        if (objects == null)
            throw new IllegalArgumentException("Cannot pass null.");

        for (T object: objects) {

            if (object == null)
                throw new IllegalArgumentException("Cannot pass null object.");
        }

        ElasticSearchController.AddItems<T> controller = new ElasticSearchController.AddItems<T>();
        this.lastTask = controller;
        controller.execute(objects);
    }

    public void delete(T... objects) {

        if (objects == null)
            throw new IllegalArgumentException("Cannot pass null.");

        for (T object: objects) {

            if (object == null)
                throw new IllegalArgumentException("Cannot pass null object.");

            if (object.getId() == null)
                throw new IllegalArgumentException("Given object has no id.");
        }

        ElasticSearchController.DeleteItems<T> controller =
                new ElasticSearchController.DeleteItems<T>();

        this.lastTask = controller;
        controller.execute(objects);
    }

    /** Delete all objects of the type this.type. */
    public void deleteAll() throws TimeoutException, ExecutionException, InterruptedException {

        SearchFilter oldFilter = this.filter;
        this.filter = null;
        ArrayList<T> results;

        do {

            results = this.getNext(0);

            for (T result: results) {

                if (result.getId() == null)
                    throw new RuntimeException("id should not be null.");

                this.delete(result);
                this.waitForTask();

                if (result.getId() != null)
                    throw new RuntimeException("Did not delete properly");
            }

        } while (results.size() > 0);

        this.filter = oldFilter;
    }

    public void refreshIndex() {

        ElasticSearchController.RefreshIndex controller =
                new ElasticSearchController.RefreshIndex();

        this.lastTask = controller;
        controller.execute();
    }
}