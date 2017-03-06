package com.csahmad.moodcloud;

import android.os.AsyncTask;
import android.util.Log;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

/**
 * Created by oahmad on 2017-03-06.
 */

public class ElasticSearchController {

    private static String url = "http://cmput301.softwareprocess.es:8080";
    private static String index = "CMPUT301W17T13";

    /** For building and executing save commands and search queries. */
    private static JestDroidClient client;

    // AsyncTask<Params, Progress, Result>
    /**
     * For saving an object using elasticsearch.
     */
    public static class AddObject<T extends ElasticSearchObject> extends AsyncTask<T, Void, Void> {

        /**
         * Saves the given object(s) using elasticsearch.
         *
         * @param items the objects to save
         * @return null
         */
        @Override
        protected Void doInBackground(T... items) {

            ElasticSearchController.setClientIfNull();        // Set up client if it is null

            // Save each object
            for (T item: items) {

                Index index = new Index.Builder(item)
                        .index(ElasticSearchController.index)
                        .type(item.getTypeName())
                        .build();

                try {

                    DocumentResult result = ElasticSearchController.client.execute(index);

                    if (result.isSucceeded())
                        item.setId(result.getId());

                    else
                        Log.i("Error", "Elasticsearch was not able to add the object");
                }

                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the objects.");
                }
            }

            return null;
        }
    }

    ;

    /** Set up client if it is null. */
    public static void setClientIfNull() {

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
