/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Calendar;

/**
 * A date and time restriction that restricts dates to be either on or before the date held by this restriction.
 */
public class OnOrBeforeDateTimeRestriction extends DateRestriction
{
    /**
     * Initialises this date restriction with the given date. Note that the hours, minutes, seconds fields of the given date are taking into account by this class.(not the Millisecond)
     * 
     * @param date
     *            The date which this restriction operates on.
     */
    public OnOrBeforeDateTimeRestriction(Calendar date)
    {
        super(date);
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

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(93, 71).append(this.date).toHashCode();
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
        if (!(obj instanceof OnOrBeforeDateTimeRestriction))
        {
            return false;
        }
        OnOrBeforeDateTimeRestriction other = (OnOrBeforeDateTimeRestriction) obj;
        return date.equals(other.date);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("OnOrBeforeDateTimeRestriction: ");
        sb.append(date);

        return sb.toString();
    }
}
