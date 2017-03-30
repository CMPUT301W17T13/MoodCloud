package com.csahmad.moodcloud;

import android.os.Parcel;
import java.util.ArrayList;

/** Read/write values from/to a {@link Parcel}. */
public class ParcelIO {

    /**
     * What to replace null values with when writing {@link Integer}s to {@link Parcel} (and what to
     * interpret as null values when reading {@link int}s from {@link Parcel}).
     */
    private static int nullIntParcelValue = -1;
    /**
     * What to replace null values with when writing {@link Double}s to {@link Parcel} (and what to
     * interpret as null values when reading {@link double}s from {@link Parcel}).
     */
    private static double nullDoubleParcelValue = -200.0d;

    /**
     * Write the given {@link SortOrder} value to the given {@link Parcel}.
     *
     * @param out the {@link Parcel} to write to
     * @param sortOrder the {@link SortOrder} value to write
     */
    public static void writeSortOrder(Parcel out, SortOrder sortOrder) {

        if (sortOrder == null)
            out.writeInt(ParcelIO.nullIntParcelValue);

        else {

            switch (sortOrder) {

                case Ascending:
                    out.writeInt(0);
                    break;

                case Descending:
                    out.writeInt(0);
                    break;

                default:
                    throw new RuntimeException("I should not be here.");
            }
        }
    }

    /**
     * Read and return a {@link SortOrder} value from the given {@link Parcel}.
     *
     * @param in the {@link Parcel} to read from
     * @return the {@link SortOrder} value read from the given {@link Parcel}
     */
    public static SortOrder readSortOrder(Parcel in) {

        Integer integerValue = ParcelIO.readInteger(in);
        if (integerValue == null) return null;

        switch (integerValue) {

            case 0:
                return SortOrder.Ascending;

            case 1:
                return SortOrder.Descending;

            default:
                throw new RuntimeException("I should not be here.");
        }
    }

    /**
     * Write the given {@link SimpleLocation} to the given {@link Parcel}.
     *
     * @param out the {@link Parcel} to write to
     * @param location the {@link SimpleLocation} to write
     */
    public static void writeLocation(Parcel out, SimpleLocation location) {

        if (location == null) {
            out.writeDouble(ParcelIO.nullDoubleParcelValue);
            out.writeDouble(ParcelIO.nullDoubleParcelValue);
            out.writeDouble(ParcelIO.nullDoubleParcelValue);
        }

        else {
            ParcelIO.writeDouble(out, location.getLatitude());
            ParcelIO.writeDouble(out, location.getLongitude());
            ParcelIO.writeDouble(out, location.getAltitude());
        }
    }

    /**
     * Read and return a {@link SimpleLocation} value from the given {@link Parcel}.
     *
     * @param in the {@link Parcel} to read from
     * @return the {@link SimpleLocation} read from the given {@link Parcel}
     */
    public static SimpleLocation readLocation(Parcel in) {

        Double latitude = ParcelIO.readDouble(in);
        Double longitude = ParcelIO.readDouble(in);
        Double altitude = ParcelIO.readDouble(in);

        if (NullTools.allNullOrEmpty(latitude, longitude, altitude))
            return null;

        return new SimpleLocation(latitude, longitude, altitude);
    }

    /**
     * Write the given {@link Integer} to the given {@link Parcel}.
     *
     * @param out the {@link Parcel} to write to
     * @param integer the {@link Integer} to write
     */
    public static void writeInteger(Parcel out, Integer integer) {

        if (integer == null)
            out.writeInt(ParcelIO.nullIntParcelValue);

        else
            out.writeInt(integer);
    }

    /**
     * Read and return an {@link Integer} value from the given {@link Parcel}.
     *
     * <p>
     * If the read integer equals {@link #nullIntParcelValue}, return null.
     *
     * @param in the {@link Parcel} to read from
     * @return the {@link Integer} read from the given {@link Parcel}
     */
    public static Integer readInteger(Parcel in) {

        int result = in.readInt();
        if (result == ParcelIO.nullIntParcelValue) return null;
        return result;
    }

    /**
     * Write the given {@link Double} to the given {@link Parcel}.
     *
     * @param out the {@link Parcel} to write to
     * @param doubleValue the {@link Double} to write
     */
    public static void writeDouble(Parcel out, Double doubleValue) {

        if (doubleValue == null)
            out.writeDouble(ParcelIO.nullDoubleParcelValue);

        else
            out.writeDouble(doubleValue);
    }

    /**
     * Read and return an {@link Double} value from the given {@link Parcel}.
     *
     * <p>
     * If the read double equals {@link #nullDoubleParcelValue}, return null.
     *
     * @param in the {@link Parcel} to read from
     * @return the {@link Double} read from the given {@link Parcel}
     */
    public static Double readDouble(Parcel in) {

        double result = in.readDouble();
        if (result == ParcelIO.nullDoubleParcelValue) return null;
        return result;
    }

    /**
     * Write the given {@link FieldValue}s to the given {@link Parcel}.
     *
     * @param out the {@link Parcel} to write to
     * @param fieldValues the {@link FieldValue}s to write
     */
    public static void writeFieldValues(Parcel out, ArrayList<FieldValue> fieldValues) {

        ArrayList<String> fields = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        int size = fieldValues.size();
        FieldValue fieldValue;

        for (int i = 0; i < size; i++) {
            fieldValue = fieldValues.get(i);
            fields.add(fieldValue.getFieldName());
            values.add(fieldValue.getValue());
        }

        out.writeStringList(fields);
        out.writeStringList(values);
    }

    /**
     * Read and return {@link FieldValue}s from the given {@link Parcel}.
     *
     * @param in the {@link Parcel} to read from
     * @return the {@link FieldValue}s read from the given {@link Parcel}
     */
    public static ArrayList<FieldValue> readFieldValues(Parcel in) {

        ArrayList<String> fields = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        in.readStringList(fields);
        in.readStringList(values);

        ArrayList<FieldValue> fieldValues = new ArrayList<FieldValue>();

        int size = fields.size();

        for (int i = 0; i < size; i++)
            fieldValues.add(new FieldValue(fields.get(i), values.get(i)));

        return fieldValues;
    }
}
