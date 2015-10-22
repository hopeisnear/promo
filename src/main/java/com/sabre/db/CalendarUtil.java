/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class CalendarUtil
{
    protected CalendarUtil()
    {
    }

    public static Calendar generateCalendar(LocalDate date)
    {
        if (date == null)
        {
            return null;
        }
        return new GregorianCalendar(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    public static Calendar generateCalendar(LocalDateTime date)
    {
        if (date == null)
        {
            return null;
        }
        return new GregorianCalendar(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), date.getHourOfDay(), date.getMinuteOfHour());
    }

    public static Calendar generateCalendar(LocalDateTime date, TimeZone zone)
    {
        if (date == null)
        {
            return null;
        }
        Calendar cal = Calendar.getInstance(zone);
        cal.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), date.getHourOfDay(), date.getMinuteOfHour());
        return cal;
    }

    public static Calendar generateCalendar(LocalDate date, TimeZone zone)
    {
        if (date == null)
        {
            return null;
        }
        Calendar cal = Calendar.getInstance(zone);
        cal.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        return cal;
    }

    public static Calendar generateCalendar(Date date)
    {
        if (date == null)
        {
            return null;
        }
        Calendar newCalendar = new GregorianCalendar();
        newCalendar.setTime(date);
        return newCalendar;
    }

    public static Calendar generateCalendar(Date date, TimeZone timezone)
    {
        Calendar calendar = generateCalendar(date);
        calendar.setTimeZone(timezone);
        return calendar;
    }

    public static Calendar generateCalendar(XMLGregorianCalendar xmlCalendar)
    {
        if (xmlCalendar == null)
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try
        {
            date = sdf.parse(xmlCalendar.toString());
        }
        catch (ParseException e)
        {
            return null;
        }
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        time.setTimeInMillis(date.getTime());
        return time;
    }

    public static void shiftToStartOfDay(Calendar cal)
    {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static void shiftToEndOfDay(Calendar cal)
    {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }

    public static Calendar getStartOfDay(Calendar cal)
    {
        if (cal == null)
        {
            return null;
        }
        Calendar newCal = (Calendar) cal.clone();
        shiftToStartOfDay(newCal);
        return newCal;
    }

    public static Calendar getEndOfDay(Calendar cal)
    {
        if (cal == null)
        {
            return null;
        }
        Calendar newCal = (Calendar) cal.clone();
        shiftToEndOfDay(newCal);
        return newCal;
    }

    public static DateTime getStartOfDay(LocalDate date, DateTimeZone timeZone)
    {
        try
        {
            return date.toDateTime(new LocalTime(0, 0, 0, 0, date.getChronology()), timeZone);
        }
        catch (IllegalFieldValueException ifex)
        {
            // for example on 2014-03-28 for Asia/Amman timezone time 00:00:00 doesn't exist due to date time change
            return date.toDateTime(new LocalTime(1, 0, 0, 0, date.getChronology()), timeZone);
        }
    }

    public static DateTime getEndOfDay(LocalDate date, DateTimeZone timeZone)
    {
        return date.toDateTime(new LocalTime(23, 59, 59, 0, date.getChronology()), timeZone);
    }

    public static String getStringValueYYYYMMDD(Calendar cal)
    {
        final String yyyy = String.valueOf(cal.get(Calendar.YEAR));
        final String mm = String.valueOf(cal.get(Calendar.MONTH) + 1);
        final String dd = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        return yyyy + (mm.length() == 1 ? "0" + mm : mm) + (dd.length() == 1 ? "0" + dd : dd);
    }

    public static String getStringValueYYYY_MM(Calendar cal)
    {
        final String yyyy = String.valueOf(cal.get(Calendar.YEAR));
        final String mm = String.valueOf(cal.get(Calendar.MONTH) + 1);
        return yyyy + "/" + (mm.length() == 1 ? "0" + mm : mm);
    }

    public static String getStringValueYYYYMMDD(LocalDate date)
    {
        return date.toString().replace("-", "");
    }

    public static Date getDateTz(Calendar cal)
    {
        if (cal == null)
        {
            return null;
        }
        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH'h'mm");
        DateFormat dateFormatterWithCalendarTimeZone = new SimpleDateFormat("MM/dd/yyyy HH'h'mm");
        dateFormatterWithCalendarTimeZone.setTimeZone(cal.getTimeZone());
        Date newDate = null;
        try
        {
            newDate = dateFormatter.parse(dateFormatterWithCalendarTimeZone.format(cal.getTime()));
        }
        catch (ParseException e)
        {
            return null;
        }
        return newDate;
    }
}
