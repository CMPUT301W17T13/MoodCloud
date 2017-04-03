package com.csahmad.moodcloud;

import android.os.Parcel;
import java.util.ArrayList;
import java.util.List;

// TODO: 2017-04-01 Instead of using/having some of the methods here, make some objects Parcelable

/** Read/write values from/to a Parcel. */
public class ParcelIO {

    /**
     * What to replace null values with when writing Integers to Parcel (and what to
     * interpret as null values when reading ints from Parcel).
     */
    private static int nullIntParcelValue = -1;
    /**
     * What to replace null values with when writing Doubles to Parcel (and what to
     * interpret as null values when reading doubles from Parcel).
     */
    private static double nullDoubleParcelValue = -200.0d;

    /**
     * Read and return a {@link GeoPoint} from the given Parcel.
     *
     * @param in the Parcel to read from
     * @return the {@link GeoPoint} read from the given Parcel
     */
    public static GeoPoint readGeoPoint(Parcel in) {

        Double lat = ParcelIO.readDouble(in);
        Double lon = ParcelIO.readDouble(in);
        if (lat == null && lon == null) return null;
        return new GeoPoint(lat, lon);
    }

    /**
     * Write the given {@link GeoPoint} to the given Parcel.
     *
     * @param out the Parcel to write to
     * @param geoPoint the {@link GeoPoint} to write
     */
    public static void writeGeoPoint(Parcel out, GeoPoint geoPoint) {

        if (geoPoint == null) {
            ParcelIO.writeDouble(out, null);
            ParcelIO.writeDouble(out, null);
        }

        else {
            ParcelIO.writeDouble(out, geoPoint.getLat());
            ParcelIO.writeDouble(out, geoPoint.getLon());
        }
    }

    /**
     * Write the given {@link SortOrder} value to the given Parcel.
     *
     * @param out the Parcel to write to
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
     * Read and return a {@link SortOrder} value from the given Parcel.
     *
     * @param in the Parcel to read from
     * @return the {@link SortOrder} value read from the given Parcel
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
     * Write the given {@link SimpleLocation} to the given Parcel.
     *
     * @param out the Parcel to write to
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
     * Read and return a {@link SimpleLocation} value from the given Parcel.
     *
     * @param in the Parcel to read from
     * @return the {@link SimpleLocation} read from the given Parcel
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
     * Write the given Integer to the given Parcel.
     *
     * @param out the Parcel to write to
     * @param integer the Integer to write
     */
    public static void writeInteger(Parcel out, Integer integer) {

        if (integer == null)
            out.writeInt(ParcelIO.nullIntParcelValue);

        else
            out.writeInt(integer);
    }

    /**
     * Read and return an Integer value from the given Parcel.
     *
     * <p>
     * If the read integer equals nullIntParcelValue, return null.
     *
     * @param in the Parcel to read from
     * @return the Integer read from the given Parcel
     */
    public static Integer readInteger(Parcel in) {

        int result = in.readInt();
        if (result == ParcelIO.nullIntParcelValue) return null;
        return result;
    }

    /**
     * Write the given Double to the given Parcel.
     *
     * @param out the Parcel to write to
     * @param doubleValue the Double to write
     */
    public static void writeDouble(Parcel out, Double doubleValue) {

        if (doubleValue == null)
            out.writeDouble(ParcelIO.nullDoubleParcelValue);

        else
            out.writeDouble(doubleValue);
    }

    /**
     * Read and return an Double value from the given Parcel.
     *
     * <p>
     * If the read double equals nullDoubleParcelValue, return null.
     *
     * @param in the Parcel to read from
     * @return the Double read from the given Parcel
     */
    public static Double readDouble(Parcel in) {

        double result = in.readDouble();
        if (result == ParcelIO.nullDoubleParcelValue) return null;
        return result;
    }

    /**
     * Write the given {@link FieldValue}s to the given Parcel.
     *
     * @param out the Parcel to write to
     * @param fieldValues the {@link FieldValue}s to write
     */
    public static void writeFieldValues(Parcel out, ArrayList<FieldValue> fieldValues) {

        if (fieldValues == null) {
            out.writeStringList(null);
            out.writeStringList(null);
            return;
        }

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
     * Read and return {@link FieldValue}s from the given Parcel.
     *
     * @param in the Parcel to read from
     * @return the {@link FieldValue}s read from the given Parcel
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

        if (fieldValues.size() == 0) return null;
        return fieldValues;
    }

    /**
     * Read and return an ArrayList of Strings from the given Parcel.
     *
     * <p>
     * If the read ArrayList is empty, return null.
     *
     * @param in the Parcel to read from
     * @return the ArrayList read from the given Parcel
     */
    public static ArrayList<String> readStringList(Parcel in) {

        ArrayList<String> list = new ArrayList<String>();
        in.readStringList(list);
        if (list.size() == 0) return null;
        return list;
    }
}
