/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Calendar;

/**
 * A date restriction that restricts dates to be either on or before the date held by this restriction.
 */
public class OnOrBeforeDateRestriction extends DateRestriction
{
    /**
     * Initialises this date restriction with the given date. Note that the hours, minutes, seconds and milliseconds fields of the given date are effectively ignored by this class, as it works on dates only, not times.
     * 
     * @param date
     *            The date which this restriction operates on.
     */
    public OnOrBeforeDateRestriction(Calendar date)
    {
        super(date);
        // Ensure the time is just before midnight on the date given
        super.date.set(Calendar.HOUR_OF_DAY, 23);
        super.date.set(Calendar.MINUTE, 59);
        super.date.set(Calendar.SECOND, 59);
        super.date.set(Calendar.MILLISECOND, 999);
    }

    /**
     * Returns true iff the given date is on or before the date held by this date restriction. The comparison honours the timezones of both dates.
     * 
     * @see com.eb2.qtrip.model.util.DateRestriction#isValid(Calendar)
     */
    @Override
    public boolean isValid(Calendar date)
    {
        return date.getTimeInMillis() <= this.date.getTimeInMillis();
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof OnOrBeforeDateRestriction))
        {
            return false;
        }
        OnOrBeforeDateRestriction other = (OnOrBeforeDateRestriction) obj;
        return this.date.equals(other.date);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(7, 7).append(this.date).toHashCode();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("OnOrBeforeDateRestriction: ");
        sb.append(super.date.toString());

        return sb.toString();
    }
}
