package com.csahmad.moodcloud;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * Created by Erick on 3/29/2017.
 * solution taken from http://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
 * on 2017-03-29
 */

public class MyViewAction {
    public static ViewAction clickChildViewWithId(final int id){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
