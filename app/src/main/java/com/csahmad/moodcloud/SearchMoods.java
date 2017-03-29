package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.concurrent.TimeoutException;

public class SearchMoods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_moods);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText findText = (EditText) findViewById(R.id.findText);

        final Spinner topSpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> topAdapter = ArrayAdapter.createFromResource(this,
                R.array.whereArray);
        topAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topSpinner.setAdapter(topAdapter);

        final Spinner moodSpinner = (Spinner) findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> moodAdapter = ArrayAdapter.createFromResource(this,
                R.array.moodsArray, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        moodSpinner.setAdapter(moodAdapter);

        final Spinner contextSpinner = (Spinner) findViewById(R.id.spinner3);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> contextAdapter = ArrayAdapter.createFromResource(this,
                R.array.groupSoloArray, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        contextSpinner.setAdapter(contextAdapter);

        Button searchButton = (Button) findViewById(R.id.searchButton);

        final Context context = this;

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String where = (String) topSpinner.getSelectedItem();
                String keyword = findText.getText().toString();
                String moodString = (String) moodSpinner.getSelectedItem();
                String contextString = (String) contextSpinner.getSelectedItem();

                SearchFilter filter = new SearchFilter();

                if (!keyword.equals("")) {
                    filter.addKeywordField("triggerText");
                    filter.addKeyword(keyword);
                }

                if (!moodString.equals("Any")) {
                    filter.setMood(Mood.fromString(moodString));
                }

                if (!contextString.equals("Any")) {
                    filter.setContext(SocialContext.fromString(contextString));
                }

                Intent intent = new Intent(context, SearchResultsActivity.class);

                if (where == "Recent Week") {
                    filter.setMaxTimeUnitsAgo(1);
                    where = "Everyone";
                }

                intent.putExtra("WHERE", where);
                intent.putExtra("FILTER", filter);
                startActivity(intent);
            }
        });

        Button findUserButton = (Button) findViewById(R.id.findUserButton);
        final EditText findUser = (EditText) findViewById(R.id.findUser);

        findUserButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    String input = findUser.getText().toString();
                    ProfileController controller = new ProfileController();
                    Profile profile = controller.getProfileFromUsername(input);

                    if (profile == null)
                        Toast.makeText(context, "No such user", Toast.LENGTH_LONG).show();

                    else {
                        Intent intent = new Intent(context, ViewProfileActivity.class);
                        intent.putExtra("ID", profile.getId());
                        startActivity(intent);
                    }
                }

                catch (TimeoutException e) {
                    Log.i("Error", "Oh no");
                }
            }
        });



    }

}
