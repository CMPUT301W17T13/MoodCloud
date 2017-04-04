package com.csahmad.moodcloud;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateConverter {

    public  static Calendar toDate(int year, int month, int day) {


    return new GregorianCalendar(year, month, day);

    }
    public  static String dateToString (Calendar date){
        //based on http://stackoverflow.com/questions/9243578/java-util-date-and-getyear/12273692#12273692
        if (date == null) return "";
        String sign = "_";
        String yearResult = Integer.toString(date.get(Calendar.YEAR));
        String monthResult = Integer.toString(date.get(Calendar.MONTH)+1);
        String dayResult = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        return yearResult + sign + monthResult + sign + dayResult;
    }
}
