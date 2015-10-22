package com.sabre.db;

import org.joda.time.DateTime;

/**
 * This implementation provided real system current time in default (system) time zone.
 */
public final class RealSystemClock implements Clock
{

    private static final RealSystemClock INSTANCE = new RealSystemClock();

    private RealSystemClock()
    {
    }

    /**
     * @return instance of {@link com.sabre.ssse.common.time.RealSystemClock}
     */
    public static RealSystemClock getInstance()
    {
        return INSTANCE;
    }

    /**
     * @return {@link DateTime} set to the current system time in the default time zone
     */
    @Override
    public DateTime getTime()
    {
        return DateTime.now();
    }
}
