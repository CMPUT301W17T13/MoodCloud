package com.csahmad.moodcloud;

import android.os.AsyncTask;
import android.util.Log;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Refresh;

// TODO: 2017-03-08 Handle exceptions better

public class ElasticSearchController {

    private static final String url = "http://cmput301.softwareprocess.es:8080";
    private static final String index = "cmput301w17t13";

    private static final int resultSize = 1;//25;

    /** For building and executing save commands and search queries. */
    private static JestDroidClient client;

    public static int getResultSize() {

        return ElasticSearchController.resultSize;
    }

    // AsyncTask<Params, Progress, Result>
    /**
     * For refreshing an elasticsearch index to ensure changes are visible everywhere.
     */
    public static class RefreshIndex extends AsyncTask<Void, Void, Void> {

        /**
         * Deletes the given object(s) using elasticsearch.
         *
         * @return null
         */
        @Override
        protected Void doInBackground(Void... nothing) {

            ElasticSearchController.setClient();        // Set up client if it is null
            ElasticSearchController.makeIndex();        // Make index if not exists

            Refresh refresh = new Refresh.Builder().addIndex(ElasticSearchController.index).build();

            try {

                JestResult result = ElasticSearchController.client.execute(refresh);

                if (!result.isSucceeded())
                    Log.i("Error", "Could not refresh");
            }

            catch (IOException e) {
                Log.i("Error", "Elasticsearch died (could not refresh)");
            }

            return null;
        }
    }

    // AsyncTask<Params, Progress, Result>
    /**
     * For deleting objects using elasticsearch.
     */
    public static class DeleteItems<T extends ElasticSearchObject>
            extends AsyncTask<T, Void, Void> {

        /**
         * Deletes the given object(s) using elasticsearch.
         *
         * @param items the objects to delete
         * @return null
         */
        @Override
        protected Void doInBackground(T... items) {

            ElasticSearchController.setClient();        // Set up client if it is null
            ElasticSearchController.makeIndex();        // Make index if not exists

            for (T item: items) {

                if (item.getId() == null)
                    throw new IllegalArgumentException("Given item has no ID.");

                Delete delete = new Delete.Builder(item.getId())
                        .index(ElasticSearchController.index)
                        .type(item.getTypeName())
                        .build();

                item.setId(null);

                try {
                    ElasticSearchController.client.execute(delete);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    // AsyncTask<Params, Progress, Result>
    /**
     * For saving or updating an object using elasticsearch.
     *
     * If the object's ID is null, add (otherwise update)
     */
    public static class AddItems<T extends ElasticSearchObject> extends AsyncTask<T, Void, Void> {

        /**
         * Saves/updates the given object(s) using elasticsearch.
         *
         * @param items the objects to save/update
         * @return null
         */
        @Override
        protected Void doInBackground(T... items) {

            ElasticSearchController.setClient();        // Set up client if it is null
            ElasticSearchController.makeIndex();        // Make index if not exists

            boolean isNew;

            // Save/update each object
            for (T item: items) {

                Index index;

                if (item == null)
                    throw new IllegalArgumentException("Cannot pass null argument.");

                if (item.getId() == null)
                    isNew = true;

                else
                    isNew = false;

                if (isNew) {

                    index = new Index.Builder(item)
                            .index(ElasticSearchController.index)
                            .type(item.getTypeName())
                            .build();
                }

                else {

                    index = new Index.Builder(item)
                            .index(ElasticSearchController.index)
                            .type(item.getTypeName())
                            .id(item.getId())
                            .build();
                }

                try {

                    DocumentResult result = ElasticSearchController.client.execute(index);

                    if (result.isSucceeded()) {

                        if (isNew)
                            item.setId(result.getId());

                        else if (item.getId() != result.getId())
                            throw new RuntimeException("Them IDs should be equal.");
                    }

                    else {
                        Log.i("Error", "Elasticsearch died: " + result.getErrorMessage());
                        item.setId("Barbie");
                    }
                }

                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the objects.");
                }
            }

            return null;
        }
    }

    // AsyncTask<Params, Progress, Result>
    /**
     * For getting objects by ID.
     */
    public static class GetById<T extends ElasticSearchObject> extends AsyncTask<String, Void, T> {

        private Class type;
        private String typeName;

        public Class getType() {

            return this.type;
        }

        public void setType(Class type) {

            this.type = type;
        }

        public String getTypeName() {

            return this.typeName;
        }

        public void setTypeName(String typeName) {

            this.typeName = typeName;
        }

        /**
         * Gets the object with the given ID.
         *
         * @param ids the first argument is the id of the object to get
         * @return the object with the given ID
         */
        @Override
        protected T doInBackground(String... ids) {

            ElasticSearchController.setClient();        // Set up client if it is null
            ElasticSearchController.makeIndex();        // Make index if not exists

            String id = ids[0];

            Get get = new Get.Builder(ElasticSearchController.index, id)
                    .type(this.typeName).build();

            try {

                DocumentResult result = ElasticSearchController.client.execute(get);

                if (result.isSucceeded())
                    return (T) result.getSourceAsObject(this.type);

                else
                    Log.i("Error", "Elasticsearch died: " + result.getErrorMessage());
            }

            catch (IOException e) {
                Log.i("Error", "Could not get object by ID.");
            }

            return null;
        }
    }

    // AsyncTask<Params, Progress, Result>
    /**
     * For searching for objects that match the given restrictions using elasticsearch.
     */
    public static class GetItems<T extends ElasticSearchObject>
            extends AsyncTask<SearchFilter, Void, ArrayList<T>> {

        private int from = 0;
        private Class type;
        private String typeName;

        public int getFrom() {

            return this.from;
        }

        public void setFrom(int from) {

            this.from = from;
        }

        public Class getType() {

            return this.type;
        }

        public void setType(Class type) {

            this.type = type;
        }

        public String getTypeName() {

            return this.typeName;
        }

        public void setTypeName(String typeName) {

            this.typeName = typeName;
        }

        /**
         * Searches for objects using the given search filter.
         *
         * @param searchFilters only pass one search filter to restrict the search (or pass no
         *                      search filters to get all objects)
         * @return the objects matching the restrictions in the given search filter
         */
        @Override
        protected ArrayList<T> doInBackground(SearchFilter... searchFilters) {

            ElasticSearchController.setClient();        // Set up client if it is null
            ElasticSearchController.makeIndex();        // Make index if not exists

            if (this.typeName == null) {

                throw new IllegalStateException(
                        "Cannot call doInBackground without setting typeName.");
            }

            if (this.type == null) {

                throw new IllegalStateException(
                        "Cannot call doInBackground without setting type.");
            }

            // Will store results (objects with the given keywords)
            ArrayList<T> results = new ArrayList<T>();

            String query;

            // I
            if (searchFilters.length == 0 || searchFilters[0] == null ||
                    !searchFilters[0].hasRestrictions()) {

                query = "";
                Log.i("Conditional", "If I'm here, should be NO searchFilter.");
            }

            // If keyword passed, make the query string (otherwise leave query as an empty string)
            //if (!keywordString.equals("")) {
            else {

                SearchFilter searchFilter = searchFilters[0];

                query = QueryBuilder.build(searchFilter, ElasticSearchController.resultSize,
                        this.from);

                Log.i("Conditional", "If I'm here, should be a searchFilter.");
            }

            Log.i("Query", "Query: " + query);

            Search search = new Search.Builder(query)
                    .addIndex(ElasticSearchController.index)
                    .addType(this.typeName)
                    .build();

            try {

                // Get the results of the query:

                SearchResult result = ElasticSearchController.client.execute(search);

                if (result.isSucceeded()) {
                    //List<T> foundObjects = result.getSourceAsObjectList(this.type);

                    List<SearchResult.Hit<T, Void>> hits = result.getHits(this.type);
                    Log.i("ListSize", "Result size: " + Integer.toString(hits.size()));

                    for (SearchResult.Hit<T, Void> hit: hits)
                        results.add(hit.source);

                    //Log.i("ListSize", "Result size: " + Integer.toString(foundObjects.size()));
                    //results.addAll(foundObjects);
                }

                else
                    Log.i("Error", "The search query failed to find any objects that matched " +
                            query);
            }

            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the" +
                        "elasticsearch server!");
            }

            return results;
        }
    }

    // Modified from this code:
    // http://www.programcreek.com/java-api-examples/index.php?api=io.searchbox.indices.IndicesExists
    // Accessed Mar 8, 2017
    /** Make index if not exists. */
    public static void makeIndex() {

        ElasticSearchController.setClient();        // Set up client if it is null

        IndicesExists indicesExists = new IndicesExists.Builder(ElasticSearchController.index)
                .build();

        try {

            boolean indexExists = ElasticSearchController.client.execute(indicesExists)
                    .isSucceeded();

            if (!indexExists) {

                CreateIndex createIndex = new CreateIndex.Builder(ElasticSearchController.index)
                        .build();

                ElasticSearchController.client.execute(createIndex);
            }
        }

        catch (IOException e) {
            Log.i("Error", "Could not make index.");
        }
    }

    /** Set up client if it is null. */
    public static void setClient() {

        if (ElasticSearchController.client == null) {

            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(
                    ElasticSearchController.url);

            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            ElasticSearchController.client = (JestDroidClient) factory.getObject();
        }
    }
}
