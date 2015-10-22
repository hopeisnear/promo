/* Copyright 2012 Sabre Holdings */
package com.sabre.db;


import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Builds a Calendar object with the same time and date fields but on a different TimeZone.<br>
 * The absolute time (getTime()) of the result is different to the original
 */
public class TimeZoneConverter implements Converter<Calendar, TimeZone>, Serializable
{
    // number of hours to offset the time by
    private int timeZoneOffset = 0;

    @Override
    public Calendar convert(Calendar context, TimeZone zone)
    {
        if (context == null || zone == null)
        {
            return context;
        }

        /*
         * First extract the date components from Date by using a Calendar.
         * 
         * Create a calendar and we will use it just to pull out the components of the Date object. Note that we can do this because it goes in and comes out the same way regardless of timezone.
         */

        int year = context.get(Calendar.YEAR);
        int month = context.get(Calendar.MONTH);
        int day = context.get(Calendar.DAY_OF_MONTH);
        int hour = context.get(Calendar.HOUR_OF_DAY);
        int minute = context.get(Calendar.MINUTE);
        int second = context.get(Calendar.SECOND);
        int milli = context.get(Calendar.MILLISECOND);

        Calendar converted = Calendar.getInstance(zone);
        converted.set(year, month, day, hour + timeZoneOffset, minute, second);
        converted.set(Calendar.MILLISECOND, milli);
        return converted;
    }

    public void setTimeZoneOffset(int timeZoneOffset)
    {
        this.timeZoneOffset = timeZoneOffset;
    }
}
