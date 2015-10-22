/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import ch.qos.logback.core.rolling.helper.RollingCalendar;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Represents a restriction on a single date. Time of day is not taken into account.
 * 
 * <p>
 * This class and all subclasses are immutable.
 */
public abstract class DateRestriction implements Serializable
{
    protected final Calendar date;
    private transient int hashcode = 0;

    /**
     * Creates a date restriction with the given date and comparator. Note that the time of day in the date will be ignored.
     * 
     * @param date
     *            The date
     */
    public DateRestriction(Calendar date)
    {
        Calendar temp = (Calendar) date.clone();
        this.date = temp;
        assert date.equals(temp);
    }

    /**
     * Gets the date that this restriction applies to.
     * 
     * @return
     */
    public final Calendar getDate()
    {
        return (Calendar) date.clone();
    }

    /**
     * Checks if the given date meets this date restriction.
     * 
     * @param date
     * @return
     */
    public abstract boolean isValid(Calendar date);

    /**
     * Determines if the date represented by this restriction is a rolling date
     * 
     * @return
     */
    public boolean isRolling()
    {
        return (date instanceof RollingCalendar);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        // Cache the hashcode result as this object is immutable and calculating the hashcode is expensive
        if (hashcode == 0)
        {
            int result = 17;
            result = 37 * result + getDate().hashCode();
            hashcode = result;
        }

        return hashcode;
    }
}
