/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.joda.time.DateTimeZone.forTimeZone;


public class RealClock implements Clock2, Serializable
{
    private TimeZone targetTimeZone;

    @Override
    public Calendar getNowAsCalendar()
    {
        return targetTimeZone != null ? getNowAsCalendar(targetTimeZone) : TimeUtils.convert(TimeProvider.now()).toCalendar();
    }

    @Override
    public Calendar getNowAsCalendar(TimeZone timeZone)
    {
        return TimeUtils.convert(TimeProvider.now()).toCalendar(timeZone);
    }

    @Override
    public LocalDateTime getNowAsLocalDateTime(TimeZone timeZone)
    {
        Date currentDate = getNowAsCalendar().getTime();
        DateMidnight current = new DateTime(currentDate).withZone(forTimeZone(timeZone)).toDateMidnight();
        return new LocalDateTime(current);
    }

    public void setTargetTimeZone(String targetTimeZone)
    {
        this.targetTimeZone = TimeZone.getTimeZone(targetTimeZone);
    }
}
