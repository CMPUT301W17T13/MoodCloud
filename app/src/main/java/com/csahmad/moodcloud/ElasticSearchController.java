package com.csahmad.moodcloud;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by oahmad on 2017-03-06.
 */

public class ElasticSearchController {

    private static String url = "http://cmput301.softwareprocess.es:8080";
    private static String index = "CMPUT301W17T13";

    /** For building and executing save commands and search queries. */
    private static JestDroidClient client;

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
