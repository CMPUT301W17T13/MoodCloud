package com.csahmad.moodcloud;

import android.util.Log;

/**
 * Created by oahmad on 2017-03-12.
 */

public class MappingBuilder {

    public static String buildNotAnalyzed(String... fields) {

        String mapping = "{\n" +
                "\"properties\": {\n";

        int lastIndex = fields.length - 1;

        for (int i = 0; i < fields.length; i++) {

            String field = fields[i];

            mapping += "\"" + field + "\": {\n" +
                "\"type\": \"string\",\n" +
                "\"index\": \"not_analyzed\"\n" +
                "}\n";

            if (i == lastIndex)
                mapping += "\n";

            else
                mapping += ",\n";
        }

        mapping += "}\n}";

        Log.i("Mapping", "Mapping: " + mapping);

        return mapping;
    }
}
