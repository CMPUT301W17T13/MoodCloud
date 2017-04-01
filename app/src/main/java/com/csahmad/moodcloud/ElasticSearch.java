package com.csahmad.moodcloud;

import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// TODO: 2017-03-08 Handle exceptions better
// TODO: 2017-03-31 Refactor controller classes that use this class to reduce repeated code
// - Maybe refactor this class too

/**
 * Get {@link ElasticSearchObject}s using elasticsearch or add/update {@link ElasticSearchObject}s
 * using elasticsearch.
 *
 * @see ElasticSearchController
 */
public class ElasticSearch<T extends ElasticSearchObject> {

    /** The type T. */
    private Class type;
    /** The name of type T as defined in the elasticsearch index. */
    private String typeName;

    /** Restricts results returned by queries. */
    private SearchFilter filter;

    /** The last {@link AsyncTask} that was executed within this ElasticSearch object. */
    private AsyncTask lastTask;
    /** How long to wait when attempting to execute a task before a timeout occurs. */
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

    /**
     * Wait for the last {@link AsyncTask} execution to finish.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public void waitForTask()
            throws ExecutionException, InterruptedException, TimeoutException {

        if (this.lastTask == null)
            throw new IllegalStateException("Nothing to wait for.");

        if (this.timeout == null)
            this.lastTask.get();

        else
            this.lastTask.get(this.timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Return the object with the given id.
     *
     * <p>
     * Return null if no object has the given id.
     *
     * @param id the id of the desired object
     * @return the object with the given id
     * @throws TimeoutException
     */
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

    /**
     * Return only one object that matches {@link #filter}.
     *
     * <p>
     * If {@link #filter} is null or has no restrictions, return any object. If no object matches
     * {@link #filter} or no objects exist yet, return null.
     *
     * @return an object that matches {@link #filter}
     * @throws TimeoutException
     */
    public T getSingleResult() throws TimeoutException {

        ArrayList<T> results = this.getNext(0, true);
        if (results.size() > 0) return results.get(0);
        return null;
    }

    /**
     * Return the number of objects matching the restrictions in {@link #filter}.
     *
     * @return the number of objects matching the restrictions in {@link #filter}
     * @throws TimeoutException
     */
    public Double getCount() throws TimeoutException {

        ElasticSearchController.GetCount<T> controller = new ElasticSearchController.GetCount<T>();
        this.lastTask = controller;
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

    /**
     * Return objects that match {@link #filter}.
     *
     * <p>
     * If {@link #filter} is null or has no restrictions, return all objects.
     *
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return objects that match {@link #filter}
     * @throws TimeoutException
     */
    public ArrayList<T> getNext(int from) throws TimeoutException {

        return this.getNext(from, false);
    }

    /**
     * Return the number of occurrences of each value for each field in
     * {@link #filter}.{@link SearchFilter#termAggregationFields}.
     *
     * @return the number of occurrences of each value for the fields in {@link #filter}
     * @throws TimeoutException
     */
    public HashMap<String, HashMap<String, Long>> getTermCounts() throws TimeoutException {

        ElasticSearchController.GetTermAggregations controller =
                new ElasticSearchController.GetTermAggregations();

        this.lastTask = controller;
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

    /**
     * Return objects that match {@link #filter}.
     *
     * <p>
     * If {@link #filter} is null or has no restrictions, return all objects.
     *
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @param singleResult whether to only return one object (or zero if there are no objects to
     *                     return)
     * @return objects that match {@link #filter}
     * @throws TimeoutException
     */
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

    /**
     * Update or add the given objects via elasticsearch.
     *
     * <p>
     * If an object has a null id, add it. If an object has a non-null id, update it.
     *
     * @param objects the objects to add or update
     */
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

    /**
     * Delete the given objects via elasticsearch.
     *
     * @param objects the objects to delete
     */
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

    /**
     * Delete all objects of type T via elasticsearch.
     *
     * @throws TimeoutException
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    /**
     * Refresh the elasticsearch index to make sure changes are reflected everywhere.
     */
    public void refreshIndex() {

        ElasticSearchController.RefreshIndex controller =
                new ElasticSearchController.RefreshIndex();

        this.lastTask = controller;
        controller.execute();
    }
}