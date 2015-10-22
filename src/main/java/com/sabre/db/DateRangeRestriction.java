/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Represents a restriction on a range of dates. Internally represented as two DateRestriction objects. The range is considered inclusive of the start and end dates.
 * <p/>
 * A date range restriction can also be open ended - that is, either the from or to dates can be null.
 * <p/>
 * DateRangeRestriction objects are immutable.
 */
public class DateRangeRestriction implements Serializable
{
    private DateRestriction restriction1;
    private DateRestriction restriction2;
    private boolean allowedRange;

    /**
     * Default constructor, for use only by the OR mapper. Use one of the other constructors instead!
     */
    public DateRangeRestriction()
    {
    }

    /**
     * Constructs a date range restriction with the given start date and finish date. The resulting restriction will enforce dates to be <i>between </i> these dates.
     * 
     * @param startDate
     *            The start date of the range
     * @param finishDate
     *            The finish date of the range
     */
    public DateRangeRestriction(Calendar startDate, Calendar finishDate)
    {
        this(startDate, finishDate, true);
    }

    /**
     * Constructs a date range restriction with the given start date and finish date. The resulting restriction will either enforce dates to be <i>between </i> these dates or <i>outside </i> these dates, depending on the
     * allowed range flag.
     * 
     * @param startDate
     *            The start date of the range
     * @param finishDate
     *            The finish date of the range
     * @param allowedRange
     *            True to restrict dates to between this range, false to restrict them to outside this range.
     */
    public DateRangeRestriction(Calendar startDate, Calendar finishDate, boolean allowedRange)
    {
        // Start or finish date can be null
        setStartDate(startDate);
        setFinishDate(finishDate);
        this.allowedRange = allowedRange;
    }

    public void setStartDate(Calendar startDate)
    {
        if (startDate != null)
        {
            startDate = (Calendar) startDate.clone();
        }
        this.restriction1 = startDate != null ? new OnOrAfterDateRestriction(startDate) : null;
    }

    public void setStartDateTime(Calendar startDate)
    {
        if (startDate != null)
        {
            startDate = (Calendar) startDate.clone();
        }
        this.restriction1 = startDate != null ? new OnOrAfterDateTimeRestriction(startDate) : null;
    }

    public void setFinishDate(Calendar finishDate)
    {
        if (finishDate != null)
        {
            finishDate = (Calendar) finishDate.clone();
        }
        this.restriction2 = finishDate != null ? new OnOrBeforeDateRestriction(finishDate) : null;
    }

    public void setFinishDateTime(Calendar finishDate)
    {
        if (finishDate != null)
        {
            finishDate = (Calendar) finishDate.clone();
        }
        this.restriction2 = finishDate != null ? new OnOrBeforeDateTimeRestriction(finishDate) : null;
    }

    /**
     * Gets the start date restriction. Typcially an OnOrAfterDateRestriction or an AfterDateRestriction.
     * 
     * @return after restriction
     * 
     *         not-null="true"
     */
    public DateRestriction getRestriction1()
    {
        return restriction1;
    }

    protected void setRestriction1(DateRestriction restriction1)
    {
        this.restriction1 = restriction1;
    }

    /**
     * Gets the end date restriction. Typcially an OnOrBeforeDateRestriction or an BeforeDateRestriction.
     * 
     * @return before restriction
     * 
     *         not-null="true"
     */
    public DateRestriction getRestriction2()
    {
        return restriction2;
    }

    protected void setRestriction2(DateRestriction restriction2)
    {
        this.restriction2 = restriction2;
    }

    /**
     * The allowed range flag indicates if this date range restriction restricts dates to be <i>within </i> the date range, or <i>outside </i> the date range.
     * 
     * @return True if this restriction restricts dates to within the date range, false if it restricts them to outside this date range.
     */
    public boolean isAllowedRange()
    {
        return allowedRange;
    }

    /**
     * Sets the allowed range flag, which controls whether this date range restricts dates to within or outside the range.
     * 
     * @param allowedRange
     *            True if this restriction is to restrict dates to within the date range, false if it is to restrict them to outside this date range.
     */
    public void setAllowedRange(boolean allowedRange)
    {
        this.allowedRange = allowedRange;
    }

    /**
     * Checks if the given date is valid for this date range. Will check whether the given date lies inside or outside of this date range depending on the allowed range flag. The check is inclusive of the endpoints. The
     * timezones of all calendars are honoured.
     * 
     * @param date
     *            The date to check
     * @return True if the given date meets this restriction, false otherwise.
     */
    public boolean isValid(Calendar date)
    {
        // If both restrictions are null then return the opposite of
        // allowedRange as the range is bad!
        if (restriction1 == null && restriction2 == null)
        {
            return !allowedRange;
        }

        // If either of the restrictions are null then assume to be 'in range'
        boolean isInRange;

        // If both dates are rolling, use the RollingRange class to do the
        // compare
        if ((restriction1 != null && restriction1.isRolling()) && (restriction2 != null && restriction2.isRolling()))
        {
            RollingRange rollingRange = new RollingRange(restriction1.getDate(), restriction2.getDate());
            isInRange = rollingRange.isInRange(date);
        }
        else
        {
            boolean restriction1isInRange = restriction1 == null || restriction1.isValid(date);
            boolean restriction2isInRange = restriction2 == null || restriction2.isValid(date);
            isInRange = restriction1isInRange && restriction2isInRange;
        }

        if (allowedRange)
        {
            return isInRange;
        }
        else
        {
            return !isInRange;
        }
    }

    /**
     * If any date is valid between start and end then return true.
     * 
     * @param start
     *            start date
     * @param end
     *            end date
     * @return true if any date is valid between start and end then return true.
     */
    public boolean isValidDateInRange(Calendar start, Calendar end)
    {
        // loop through all dates until we find something in range.
        // otherwise you'll have to look at that rolling date range logic.

        for (Calendar day = (Calendar) start.clone(); day.compareTo(end) <= 0; day.add(Calendar.DATE, 1))
        {
            if (isValid(day))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof DateRangeRestriction))
        {
            return false;
        }
        DateRangeRestriction other = (DateRangeRestriction) obj;
        return (restriction1 != null ? restriction1.equals(other.getRestriction1()) : other.getRestriction1() == null)
                && (restriction2 != null ? restriction2.equals(other.getRestriction2())
                        : other.getRestriction2() == null) && allowedRange == other.isAllowedRange();
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (restriction1 != null ? restriction1.hashCode() : 0);
        result = 37 * result + (restriction2 != null ? restriction2.hashCode() : 0);
        result = 37 * result + (allowedRange ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if (restriction1 != null)
        {
            builder.append("restriction1: ").append(restriction1.toString()).append(" // ");
        }
        if (restriction2 != null)
        {
            builder.append("restriction2: ").append(restriction2.toString()).append("// ");
        }
        builder.append("allowedRange: ").append(allowedRange);
        return builder.toString();
    }
}
