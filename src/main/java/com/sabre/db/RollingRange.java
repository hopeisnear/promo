/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Represents a range of rolling dates. Can pass any implementation of Calendar and the year component of the date will be ignored.
 * 
 * $Id$
 */
public class RollingRange implements Serializable
{
    private Calendar fromDate;
    private Calendar toDate;

    public RollingRange(Calendar fromDate, Calendar toDate)
    {
        setFromDate(fromDate);
        setToDate(toDate);
    }

    /**
     * Determines if the given date is in range, regardless of the year value.
     * 
     * @param date
     */
    public synchronized boolean isInRange(Calendar date)
    {
        // Ensure the from and to dates have the relevant year for the given date.
        calculateRangeForDate(date);

        // Now check if the given date is in range
        if (date.before(fromDate))
        {
            return false;
        }

        if (date.after(toDate))
        {
            return false;
        }

        return true;
    }

    /**
     * Sets the range dates in the object for the given date.
     * 
     * @param date
     */
    public synchronized void calculateRangeForDate(Calendar date)
    {
        // Set the year of the to and from Dates to be the same as the given date
        toDate.set(Calendar.YEAR, date.get(Calendar.YEAR));

        // If the toDate is before the given date, roll it to next year.
        if (toDate.before(date))
        {
            toDate.add(Calendar.YEAR, 1);
        }

        // Set the from date to have the same year as the toDate
        // If it isn't BEFORE the toDate, subtract a year
        fromDate.set(Calendar.YEAR, toDate.get(Calendar.YEAR));
        if (toDate.before(fromDate))
        {
            fromDate.add(Calendar.YEAR, -1);
        }
    }

    protected void setFromDate(Calendar fromDate)
    {
        this.fromDate = new GregorianCalendar(fromDate.getTimeZone());
        this.fromDate.setTime(fromDate.getTime());

        // Set time component to first thing in the morning
        this.fromDate.set(Calendar.HOUR_OF_DAY, 0);
        this.fromDate.set(Calendar.MINUTE, 0);
        this.fromDate.set(Calendar.SECOND, 0);
        this.fromDate.set(Calendar.MILLISECOND, 0);
    }

    protected void setToDate(Calendar toDate)
    {
        this.toDate = new GregorianCalendar(toDate.getTimeZone());
        this.toDate.setTime(toDate.getTime());

        // Set time component to last thing at night
        this.toDate.set(Calendar.HOUR_OF_DAY, 23);
        this.toDate.set(Calendar.MINUTE, 59);
        this.toDate.set(Calendar.SECOND, 59);
        this.toDate.set(Calendar.MILLISECOND, 999);
    }

    public Calendar getFromDate()
    {
        return fromDate;
    }

    public Calendar getToDate()
    {
        return toDate;
    }
}
