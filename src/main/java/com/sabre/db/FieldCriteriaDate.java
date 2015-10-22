/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class FieldCriteriaDate extends FieldCriteria<DatePair, Date> implements Serializable
{
    private DatePair date1;
    private DatePair date2;
    private DatePair date3;
    private DatePair date4;
    private DatePair date5;
    private DatePair date6;
    private DatePair date7;
    private DatePair date8;
    private DatePair date9;
    private DatePair date10;

    // Holds the maximum number of DatePair objects that are allowed for a FieldCriteriaDate
    public static int NUM_ALLOWED_DATES = 10;

    private TimeZone defaultTimeZone = null;

    private static final TimeZoneConverter timezoneConv = new TimeZoneConverter();

    public boolean isValid(Date date, TimeZone timeZone)
    {
        return isValid(Arrays.asList(date), timeZone);
    }

    public boolean isValid(Collection<Date> o, TimeZone overrideTimeZone)
    {
        if (!isDefined())
        {
            return true;
        }
        if (overrideTimeZone == null && defaultTimeZone == null)
        {
            return super.isValid(o);
        }

        return super.isValid(shiftDates(overrideTimeZone, o));
    }

    /**
     * If for any date equal to or greater than today's date only a false validity can be returned this method's response will be true.
     * 
     * @return
     */
    public boolean noLongerPossibleToBeValid()
    {
        if (getFieldsCount() == 0)
        {
            return false;
        }

        // firstly make the naive assumption that if there are excludes it
        // will possibly be valid at some point in the future
        if (getExcludes().size() > 0)
        {
            return false;
        }

        for (DatePair datePair : getIncludes())
        {
            if (datePair != null)
            {
                if (datePair.getEnd().after(new Date()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValid(LocalDate startDate, LocalDate endDate)
    {
        DateTimeZone tz = DateTimeZone.forTimeZone(TimeZone.getDefault());
        Date start = CalendarUtil.getStartOfDay(startDate, tz).toDate();
        Date end = CalendarUtil.getEndOfDay(endDate, tz).toDate();

        for (DatePair excluded : getExcludes())
        {
            if (excluded.getStart().before(end) && excluded.getEnd().after(start))
            {
                return false;
            }
        }
        for (DatePair includes : getIncludes())
        {
            if (includes.beforeOrSameAs(start) && includes.afterOrSameAs(end))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert date o that was originally in timezone of overrideTimeZone into an equivalent date (DD/MM/YYYY) in the new server timezone.
     * 
     * @param overrideTimeZone
     * @param o
     * @return
     */
    protected Collection<Date> shiftDates(TimeZone overrideTimeZone, Collection<Date> o)
    {
        Collection<Date> shiftedDates = new ArrayList<Date>();

        TimeZone timeZone = overrideTimeZone;
        if (timeZone == null)
        {
            timeZone = defaultTimeZone;
        }

        for (Date date : o)
        {
            Calendar cal = new GregorianCalendar(timeZone);
            cal.setTime(date);
            shiftedDates.add(timezoneConv.convert(cal, TimeZone.getDefault()).getTime());
        }
        return shiftedDates;
    }

    @Override
    protected boolean checkMatches(DatePair value, Date paramObject)
    {
        if (value.isInside())
        {
            return value.contains(paramObject);
        }
        else
        {
            return !value.contains(paramObject);
        }
    }

    @Override
    public Collection<DatePair> getIncludes()
    {
        if (includesPrecalculated == null)
        {
            Collection<DatePair> includes = new ArrayList<DatePair>();

            for (DatePair datePair : removeNulls(Arrays.asList(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10)))
            {
                if (datePair.isInside())
                {
                    includes.add(datePair);
                }
            }

            includesPrecalculated = includes;
            return includes;
        }
        else
        {
            return includesPrecalculated;
        }
    }

    private Collection<DatePair> includesPrecalculated;

    public Collection<DatePair> getAllDates()
    {
        return removeNulls(Arrays.asList(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10));
    }

    @Override
    public Collection<DatePair> getExcludes()
    {
        if (excludesPrecalculated == null)
        {
            Collection<DatePair> excludes = new ArrayList<DatePair>();

            for (DatePair datePair : removeNulls(Arrays.asList(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10)))
            {
                if (!datePair.isInside())
                {
                    excludes.add(datePair);
                }
            }

            excludesPrecalculated = excludes;
            return excludes;
        }
        else
        {
            return excludesPrecalculated;
        }
    }

    private Collection<DatePair> excludesPrecalculated;

    @Override
    public void addExcludeField(DatePair datePair)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;

        datePair.setInside(false);

        if (date1 == null)
        {
            date1 = datePair;
        }
        else if (date2 == null)
        {
            date2 = datePair;
        }
        else if (date3 == null)
        {
            date3 = datePair;
        }
        else if (date4 == null)
        {
            date4 = datePair;
        }
        else if (date5 == null)
        {
            date5 = datePair;
        }
        else if (date6 == null)
        {
            date6 = datePair;
        }
        else if (date7 == null)
        {
            date7 = datePair;
        }
        else if (date8 == null)
        {
            date8 = datePair;
        }
        else if (date9 == null)
        {
            date9 = datePair;
        }
        else if (date10 == null)
        {
            date10 = datePair;
        }
        else
        {
            throw new IllegalArgumentException("too many dates");
        }

    }

    @Override
    public void addIncludeField(DatePair includeField)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;

        includeField.setInside(true);

        if (date1 == null)
        {
            date1 = includeField;
        }
        else if (date2 == null)
        {
            date2 = includeField;
        }
        else if (date3 == null)
        {
            date3 = includeField;
        }
        else if (date4 == null)
        {
            date4 = includeField;
        }
        else if (date5 == null)
        {
            date5 = includeField;
        }
        else if (date6 == null)
        {
            date6 = includeField;
        }
        else if (date7 == null)
        {
            date7 = includeField;
        }
        else if (date8 == null)
        {
            date8 = includeField;
        }
        else if (date9 == null)
        {
            date9 = includeField;
        }
        else if (date10 == null)
        {
            date10 = includeField;
        }
        else
        {
            throw new IllegalArgumentException("too many dates");
        }
        checkCriteriaState();
    }

    public void addDateField(DatePair includeField)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;

        if (date1 == null)
        {
            date1 = includeField;
        }
        else if (date2 == null)
        {
            date2 = includeField;
        }
        else if (date3 == null)
        {
            date3 = includeField;
        }
        else if (date4 == null)
        {
            date4 = includeField;
        }
        else if (date5 == null)
        {
            date5 = includeField;
        }
        else if (date6 == null)
        {
            date6 = includeField;
        }
        else if (date7 == null)
        {
            date7 = includeField;
        }
        else if (date8 == null)
        {
            date8 = includeField;
        }
        else if (date9 == null)
        {
            date9 = includeField;
        }
        else if (date10 == null)
        {
            date10 = includeField;
        }
        else
        {
            throw new IllegalArgumentException("too many dates");
        }
        checkCriteriaState();
    }

    @Override
    public void clearFields()
    {
        date1 = null;
        date2 = null;
        date3 = null;
        date4 = null;
        date5 = null;
        date6 = null;
        date7 = null;
        date8 = null;
        date9 = null;
        date10 = null;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate1()
    {
        return date1;
    }

    public void setDate1(DatePair date1)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date1 = date1;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate2()
    {
        return date2;
    }

    public void setDate2(DatePair date2)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date2 = date2;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate3()
    {
        return date3;
    }

    public void setDate3(DatePair date3)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date3 = date3;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate4()
    {
        return date4;
    }

    public void setDate4(DatePair date4)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date4 = date4;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate5()
    {
        return date5;
    }

    public void setDate5(DatePair date5)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date5 = date5;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate6()
    {
        return date6;
    }

    public void setDate6(DatePair date6)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date6 = date6;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate7()
    {
        return date7;
    }

    public void setDate7(DatePair date7)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date7 = date7;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate8()
    {
        return date8;
    }

    public void setDate8(DatePair date8)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date8 = date8;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate9()
    {
        return date9;
    }

    public void setDate9(DatePair date9)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date9 = date9;
    }

    /**
     * @return field
     * 
     */
    public DatePair getDate10()
    {
        return date10;
    }

    public void setDate10(DatePair date10)
    {
        excludesPrecalculated = null;
        includesPrecalculated = null;
        this.date10 = date10;
    }

    public TimeZone getDefaultTimeZone()
    {
        return defaultTimeZone;
    }

    public void setDefaultTimeZone(TimeZone defaultTimeZone)
    {
        this.defaultTimeZone = defaultTimeZone;
    }
}
