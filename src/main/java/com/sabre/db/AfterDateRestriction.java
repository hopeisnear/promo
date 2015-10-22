/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Calendar;

/**
 * A date restriction that restricts dates to after the date held by this restriction.
 */
public class AfterDateRestriction extends DateRestriction
{
    /**
     * Initialises this date restriction with the given date. Note that the hours, minutes, seconds and milliseconds fields of the given date are effectively ignored by this class, as it works on dates only, not times.
     * 
     * @param date
     *            The date which this restriction operates on.
     */
    public AfterDateRestriction(Calendar date)
    {
        super(date);
        super.date.set(Calendar.HOUR_OF_DAY, 23);
        super.date.set(Calendar.MINUTE, 59);
        super.date.set(Calendar.SECOND, 59);
        super.date.set(Calendar.MILLISECOND, 999);
    }

    @Override
    public boolean isValid(Calendar date)
    {
        return date.after(this.date);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(15, 39).append(this.date).toHashCode();
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
        if (!(obj instanceof AfterDateRestriction))
        {
            return false;
        }
        AfterDateRestriction other = (AfterDateRestriction) obj;
        return super.date.equals(other.date);
    }
}
