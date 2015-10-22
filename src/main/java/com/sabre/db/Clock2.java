/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * An abstract mechanism for getting the current time. Being an interface, this can be mocked out for testing so that we can return a preconfigured Calendar as the current time, so that unit tests are not dependant on
 * the time of day/month/year that they are run.
 */
public interface Clock2
{
    Calendar getNowAsCalendar();

    Calendar getNowAsCalendar(TimeZone timeZone);

    LocalDateTime getNowAsLocalDateTime(TimeZone timeZone);
}
