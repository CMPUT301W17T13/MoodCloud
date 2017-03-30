package com.csahmad.moodcloud;

import android.os.AsyncTask;
import android.util.Log;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;
//mwschafe commented out unused import statements

import io.searchbox.client.JestResult;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Refresh;
import io.searchbox.indices.mapping.PutMapping;

// TODO: 2017-03-08 Handle exceptions better

/**
 * Get {@link ElasticSearchObject}s using elasticsearch or add/update {@link ElasticSearchObject}s
 * using elasticsearch.
 *
 * @see ElasticSearch
 */
public class ElasticSearchController {

    private static final String url = "http://cmput301.softwareprocess.es:8080";
    private static final String index = "cmput301w17t13";

    /** How many results to return at a time when returning objects. */
    private static final int resultSize = 25;

    /** For building and executing elasticsearch commands/queries. */
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
         * Refresh the elasticsearch index.
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

                if (item.isDummy()) continue;

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
     * For adding or updating objects using elasticsearch.
     *
     * <p>
     * If an object's ID is null, add (otherwise update).
     */
    public static class AddItems<T extends ElasticSearchObject> extends AsyncTask<T, Void, Void> {

        /**
         * Adds/updates the given objects using elasticsearch.
         *
         * @param items the objects to add/update
         * @return null
         */
        @Override
        protected Void doInBackground(T... items) {

            ElasticSearchController.setClient();        // Set up client if it is null
            ElasticSearchController.makeIndex();        // Make index if not exists

            boolean isNew;

            // Save/update each object
            for (T item: items) {

                if (item.isDummy()) continue;

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

                        if (isNew) {
                            item.setId(result.getId());
                            Log.i("ID", "New id: " + item.getId());
                        }

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
     * For getting an object by ID.
     */
    public static class GetById<T extends ElasticSearchObject> extends AsyncTask<String, Void, T> {

        /** The type T. */
        private Class type;
        /** The name of type T as defined in the elasticsearch index. */
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
         * Returns the object with the given ID.
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

                if (result.isSucceeded()) {
                    T resultObject = (T) result.getSourceAsObject(this.type);
                    resultObject.setId(result.getId());
                    return resultObject;
                }

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
     * For counting the number of objects that match the given restrictions.
     */
    public static class GetCount<T extends ElasticSearchObject>
            extends AsyncTask<SearchFilter, Void, Double> {

        /** The name of type T as defined in the elasticsearch index. */
        private String typeName;

        public String getTypeName() {

            return this.typeName;
        }

        public void setTypeName(String typeName) {

            this.typeName = typeName;
        }

        /**
         * Returns the number of objects that match the restrictions in the given search filter.
         *
         * @param searchFilters only pass one search filter to restrict the search (or pass no
         *                      search filters to get all objects)
         * @return the number of objects matching the restrictions in the given search filter
         */
        @Override
        protected Double doInBackground(SearchFilter... searchFilters) {

            String query;

            if (searchFilters.length == 0 || searchFilters[0] == null ||
                    !searchFilters[0].hasRestrictions())

                query = "";

            // If keyword passed, make the query string (otherwise leave query as an empty string)
            //if (!keywordString.equals("")) {
            else {
                SearchFilter searchFilter = searchFilters[0];
                query = QueryBuilder.build(searchFilter, ElasticSearchController.resultSize, 0);
            }

            Count count = new Count.Builder()
                    .addIndex(ElasticSearchController.index)
                    .addType(this.typeName)
                    .query(query)
                    .build();

            try {

                CountResult result = ElasticSearchController.client.execute(count);

                if (result.isSucceeded()) {
                    return result.getCount();
                }

                else
                    Log.i("Error", "Elasticsearch died with " + result.getErrorMessage());
            }

            catch (IOException e) {
                Log.i("Error", "Something went wrong trying to get count from elasticsearch.");
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

        /** Whether to only return one object (or zero if there are no objects to return). */
        private boolean singleResult = false;
        /**
         * Set to 0 to get the first x number of results, set to x to get the next x number of
         * results, set to 2x to get the next x number of results after that, and so on.
         */
        private int from = 0;
        /** The type T. */
        private Class type;
        /** The name of type T as defined in the elasticsearch index. */
        private String typeName;

        public boolean isSingleResult() {

            return this.singleResult;
        }

        public void setSingleResult(boolean singleResult) {

            this.singleResult = singleResult;
        }

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

                int resultSize = ElasticSearchController.resultSize;
                if (this.singleResult) resultSize = 1;

                query = QueryBuilder.build(searchFilter, resultSize, this.from);

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

                    List<SearchResult.Hit<T, Void>> hits = result.getHits(this.type);
                    Log.i("ListSize", "Result size: " + Integer.toString(hits.size()));

                    for (SearchResult.Hit<T, Void> hit: hits) {

                        T object = hit.source;
                        object.setId(hit.id);

                        if (object.getId() == null)
                            Log.i("Error", "ID should not be null!");

                        results.add(object);
                    }
                }

                else
                    Log.i("Error", "Elasticsearch died with: " + result.getErrorMessage());
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
    /** Make the index if it does not exist. */
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

                ElasticSearchController.makeMappings();
            }
        }

        catch (IOException e) {
            Log.i("Error", "Could not make index.");
        }
    }

    /** Make mappings for the index. */
    public static void makeMappings() {

        ElasticSearchController.makeMapping("account",
                MappingBuilder.buildNotAnalyzed("username"));

        ElasticSearchController.makeMapping("follow",
                MappingBuilder.buildNotAnalyzed("followerId", "followeeId"));

        ElasticSearchController.makeMapping("followRequest",
                MappingBuilder.buildNotAnalyzed("followerId", "followeeId"));

        ElasticSearchController.makeMapping("post",
                MappingBuilder.buildNotAnalyzed("posterId"));
    }

    /** Make a mapping for an object. */
    public static void makeMapping(String type, String mapping) {

        PutMapping putMapping = new PutMapping.Builder(ElasticSearchController.index, type,
                mapping).build();

        Log.i("Mapping", mapping);

        try {

            JestResult result = ElasticSearchController.client.execute(putMapping);

            if (!result.isSucceeded())
                Log.i("Error", "Not succeeded (create mapping): " + result.getErrorMessage());
        }

        catch (IOException e) {
            Log.i("Error", "Could not create mapping.");
        }
    }

    /** Set up the client if it is null. */
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
