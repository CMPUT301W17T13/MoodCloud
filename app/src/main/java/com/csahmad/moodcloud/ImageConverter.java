package com.csahmad.moodcloud;

/**
 * Created by linghou on 2017-03-30.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/** Convert between {@link Bitmap}s and {@link String}s. */
public class ImageConverter {

    // Taken from:
    // https://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string/9224180#9224180
    // (StackOverflow user jeet, edited by Graeme)
    // Accessed March 30, 2017
    /**
     * Convert the given {@link Bitmap} image to a {@link String} in base64 format.
     *
     * @param image the image to convert
     * @return the image in base64 format as a {@link String}
     */
    public static String toString(Bitmap image) {

        if (image == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // TODO: 2017-03-30 Fill out (currently only returns null)
    // Taken from:
    // https://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview/4837293#4837293
    // (StackOverflow user user432209)
    // Accessed March 30, 2017
    /**
     * Convert the given {@link String} to a {@link Bitmap} image.
     *
     * <p>
     * Return null if the {@link String} could not be converted.
     *
     * @param imageBase64 the image in base64 format as a {@link String}
     * @return the converted image
     */
    public static Bitmap toBitmap(String imageBase64) {

        if (imageBase64 == null) return null;

        try {
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        catch (Exception e) {
            return null;
        }
    }
}
