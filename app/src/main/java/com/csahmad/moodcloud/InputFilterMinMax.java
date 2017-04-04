package com.csahmad.moodcloud;

import android.text.InputFilter;
import android.text.Spanned;

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

            String inputString;
            double input;
            String deleted = "";

            // If text was deleted, return (keep) the deleted text (unless it is "-")
            if (source.toString().equals("")) {

                deleted = dest.toString().substring(dstart, dend);

                if (deleted.equals("-"))
                    return "";

                else

                    inputString =
                            dest.toString().substring(0, dstart) + dest.toString().substring(dend);
            }

            else
                inputString = dest.toString() + source.toString();

            input = Double.parseDouble(inputString);

            if (isInRange(this.min, this.max, input))
                return null;

            else if (deleted.toString().equals("."))
                return deleted;
        }

        catch (NumberFormatException nfe) {

            if (source.toString().equals("-"))
                return null;
        }

        return "";
    }

    private static boolean isInRange(double min, double max, double input) {

        if (input > max || input < min) return false;
        return true;
    }
}