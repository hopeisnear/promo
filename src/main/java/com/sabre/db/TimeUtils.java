package com.sabre.db;

import org.joda.time.DateTime;

/**
 * Class provides handy static methods that can be used to manipulate on dates and times
 * like i.e. additional converts for {@link DateTime}
 */
public class TimeUtils
{
    /**
     * Creates {@link com.sabre.ssse.common.time.utils.ConvertibleDateTime} instance with given {@link DateTime}
     * <p>This should be used like this: <code>TimeUtils.convert(TimeProvider.now()).toCalendar()</code>
     *
     * @param dateTime - given {@link DateTime}
     * @return {@link com.sabre.ssse.common.time.utils.ConvertibleDateTime} that provides converters for given {@link DateTime}
     */
    public static ConvertibleDateTime convert(DateTime dateTime)
    {
        return new ConvertibleDateTime(dateTime);
    }
}
