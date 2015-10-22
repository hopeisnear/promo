package com.sabre.db;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is simple {@link DateTime}  wrapper
 * that provides additional converters to other date and time formats
 * that are not accessible directly from {@link DateTime} class
 */
public class ConvertibleDateTime
{
    private DateTime dateTime;

    /**
     * Creates {@link com.sabre.ssse.common.time.utils.ConvertibleDateTime} instance with given {@link DateTime}
     * @param dateTime given dateTime to wrap
     */
    public ConvertibleDateTime(DateTime dateTime)
    {
        this.dateTime = dateTime;
    }

    /**
     * Converts this {@link DateTime} to {@link Calendar} using default {@link Locale}
     * @return  Calendar initialised with this {@link DateTime}'s millis, time zone  and default locale
     */
    public Calendar toCalendar()
    {
        return dateTime == null ? null : dateTime.toCalendar(Locale.getDefault());
    }

    /**
     * Converts this {@link DateTime} to {@link Calendar}
     *     using default {@link Locale} and given (@link java.util.TimeZone}
     * @param timeZone - given time zone
     * @return  Calendar initialised with this {@link DateTime}'s millis, given time zone and default locale
     */
    public Calendar toCalendar(TimeZone timeZone)
    {
        return dateTime == null ? null : dateTime.toDateTime(DateTimeZone.forTimeZone(timeZone)).toCalendar(Locale.getDefault());
    }

    /**
     * Converts this {@link DateTime} to {@link DateTime} using given time zone.
     * @param timeZone - given time zone
     * @return new {@link DateTime} initialized
     *     with this {@link DateTime}'s time  and given time zone
     */
    public DateTime toDateTime(TimeZone timeZone)
    {
        return dateTime == null ? null : dateTime.toDateTime(DateTimeZone.forTimeZone(timeZone));
    }

    /**
     * @return this instance {@link DateTime}
     */
    public DateTime getDateTime()
    {
        return dateTime;
    }
}
