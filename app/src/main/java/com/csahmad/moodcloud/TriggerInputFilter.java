package com.csahmad.moodcloud;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

// Modified from: https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android/14212734#14212734
// (StackOverflow user Pratik Sharma)
// Accessed April 3, 2017
/** Used to restrict numeric EditText fields to only allow input within a certain range. */
public class TriggerInputFilter implements InputFilter {

    private int maxLength, wordCount;

    public TriggerInputFilter(int maxLength, int wordCount) {

        this.maxLength = maxLength;
        this.wordCount = wordCount;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {

        String sourceString = source.toString();

        if (isValid(this.maxLength, this.wordCount, dest.toString() + sourceString, sourceString))
            return null;

        return "";
    }

    private static boolean isValid(int maxLength, int wordCount, String input, String source) {

        if (input.length() > maxLength) return false;

        int count = input.split("\\s+").length;

        if (count > wordCount || (count == wordCount && source.equals(" ")))
            return false;

        return true;
    }
}