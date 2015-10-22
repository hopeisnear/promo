/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Calendar;

public class OnOrAfterDateTimeRestriction extends DateRestriction
{
    /**
     * Initialises this date restriction with the given date. Note that the hours, minutes, seconds and milliseconds fields of the given date are effectively ignored by this class, as it works on dates only, not times.
     * 
     * @param date
     *            The date which this restriction operates on.
     */
    public OnOrAfterDateTimeRestriction(Calendar date)
    {
        super(date);
    }


    @Override
    public boolean isValid(Calendar date)
    {
        return date.getTimeInMillis() >= this.date.getTimeInMillis();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(9, 7).append(this.date).toHashCode();
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
        if (!(obj instanceof OnOrAfterDateTimeRestriction))
        {
            return false;
        }
        OnOrAfterDateTimeRestriction other = (OnOrAfterDateTimeRestriction) obj;
        return this.date.equals(other.date);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("OnOrAfterDateTimeRestriction: ");
        sb.append(super.date.toString());

        return sb.toString();
    }
}
