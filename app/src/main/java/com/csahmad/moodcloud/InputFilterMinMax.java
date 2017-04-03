package com.csahmad.moodcloud;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

// Modified from: https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android/14212734#14212734
// (StackOverflow user Pratik Sharma)
// Accessed April 3, 2017
/** Used to restrict numeric EditText fields to only allow input within a certain range. */
public class InputFilterMinMax implements InputFilter {

    private double min, max;

    public InputFilterMinMax(double min, double max) {

        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {

        try {

            double input = Double.parseDouble(dest.toString() + source.toString());

            if (isInRange(min, max, input))
                return null;
        }

        catch (NumberFormatException nfe) {
            return null;
        }

        return "";
    }

    private boolean isInRange(double min, double max, double input) {

        if (input > max || input < min) return false;
        Log.i("Min", Double.toString(min));
        Log.i("Max", Double.toString(max));
        Log.i("Input", Double.toString(input));
        return true;
    }
}