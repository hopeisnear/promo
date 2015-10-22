/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * GregorianCalendar which optionally allows the year to "roll". That is, you can construct it without specifying a particular year, and the year will be determined at runtime using the following algorithm:
 * <p/>
 * <ol>
 * <li>Determine current year and date from system clock</li>
 * <li>Assign currentYear to this calendar</li>
 * <li>If the date represented by this calendar is now in the past according to the current system date, then add one to the year in this calendar.</li>
 * </ol>
 * <p/>
 * <p/>
 * As a result, the date represented by a rolling calendar is always in the future. The above calculations are all performed using the current Timezone of this calendar.
 * <p/>
 * <p/>
 * Note that if you create this calendar as a "non-rolling" calendar, the behaviour is exactly the same as GregorianCalendar.
 * <p/>
 * <p/>
 * Note that the actual year is never actually stored in the calendar - it is purely a derived attribute.
 */
public class RollingCalendar extends GregorianCalendar
{
    // Internal flag to prevent recursive stack overflow
    private boolean rolling = false;

    private Clock2 clock = new RealClock();

    /**
     * Creates a rolling calendar initialized to the current date.
     */
    public RollingCalendar()
    {
        super();
    }

    /**
     * Creates a calendar, specifying the timezone
     */
    public RollingCalendar(TimeZone timeZone)
    {
        super(timeZone);
    }

    /**
     * Creates a calendar, specifying if its rolling or not.
     */
    public RollingCalendar(boolean isRolling)
    {
        this(isRolling, null);
    }

    public RollingCalendar(boolean isRolling, Clock2 clock)
    {
        super();
        if (clock != null)
        {
            this.clock = clock;
            this.setTimeZone(clock.getNowAsCalendar().getTimeZone());
            this.setTimeInMillis(clock.getNowAsCalendar().getTimeInMillis());
        }
    }

    /**
     * Creates a rolling calendar.
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     */
    public RollingCalendar(int year, int month, int dayOfMonth)
    {
        this(year, month, dayOfMonth, null);
    }

    public RollingCalendar(int year, int month, int dayOfMonth, Clock2 clock)
    {
        super(year, month, dayOfMonth);
        if (clock != null)
        {
            this.clock = clock;
        }
        if (year == 0)
        {
            this.set(Calendar.YEAR, clock.getNowAsCalendar().get(Calendar.YEAR));
        }
    }

    /**
     * Creates a rolling calendar.
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hourOfDay
     * @param minute
     * @param second
     */
    public RollingCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)
    {
        super(year, month, dayOfMonth, hourOfDay, minute, second);
    }

    /**
     * Returns super.get(int) for all field except YEAR, which is determined dynamically such that the date represented by this calendar is never in the past (relative to the Timezone of this calendar).
     * 
     * @see Calendar#get(int)
     */
    @Override
    public int get(int field)
    {
        // Ensure the year is set if that's the requested field
        if (field == YEAR)
        {
            setRolledYear();
        }

        return super.get(field);
    }

    @Override
    public long getTimeInMillis()
    {
        // Ensure the correct year is set before making the calculation
        setRolledYear();
        return super.getTimeInMillis();
    }

    public Date getTime(int year)
    {
        Calendar cal = clock.getNowAsCalendar(getTimeZone());
        cal.set(year, get(Calendar.MONTH), get(Calendar.DATE), get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), get(Calendar.SECOND));
        return cal.getTime();
    }

    /**
     * Ensures that the year is set to the rolled year whenever a public get method is called
     */
    protected void setRolledYear()
    {
        // Don't execute this method if we're currently 'rolling'
        if (isRolling())
        {
            return;
        }
        // Start rolling
        startRolling();

        // Get a calendar for the current date.
        Calendar currentDate = clock.getNowAsCalendar(this.getTimeZone());
        int currentYear = currentDate.get(YEAR);

        // Create a temporary clone of this calendar so we can
        // set a year on it for comparison
        GregorianCalendar temp = (GregorianCalendar) this.clone();
        temp.set(Calendar.YEAR, currentYear);

        // If the actual year stored in the object is less than the current year
        // We need to add 1 to the current year
        int correctYear = temp.before(currentDate) ? currentYear + 1 : currentYear;

        // If the year currently stored internally by the calendar object isn't the current year then set it
        if (super.get(YEAR) != correctYear)
        {
            set(YEAR, correctYear);
        }

        // Stop rolling
        stopRolling();
    }

    private void startRolling()
    {
        rolling = true;
    }

    private void stopRolling()
    {
        rolling = false;
    }

    private boolean isRolling()
    {
        return rolling;
    }

    public void setClock(Clock2 clock)
    {
        this.clock = clock;
    }
}
