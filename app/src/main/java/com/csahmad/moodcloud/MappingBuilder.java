package com.csahmad.moodcloud;

import android.util.Log;

/** Builds type mappings for the elasticsearch index. */
public class MappingBuilder {

    /** Build a portion of a mapping indicating the given string fields should not be analyzed. */
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
