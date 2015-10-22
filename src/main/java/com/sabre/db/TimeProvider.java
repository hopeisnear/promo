package com.sabre.db;

import org.joda.time.DateTime;

/**
 * This class provides static access to system time.
 *
 * <p>This should be used whenever you need access to current time for business logic that you may need
 * to mock at some time for test purposes.
 */
public final class TimeProvider
{
    private static final Clock CLOCK = ConfigurableThreadLocalClock.getInstance();

    private TimeProvider()
    {
    }

    /**
     * @return globally used default {@link com.sabre.ssse.common.time.Clock} implementation instance
     */
    public static Clock getClock()
    {
        return CLOCK;
    }

    /**
     * This method provides current time on the basis of globally configured {@link com.sabre.ssse.common.time.Clock}.
     *
     * <p>By default this time is real system time and time zone is default system time zone.
     *
     * <p>This method should be used to get current time for business logic where mocking (fixing) could bring value from the test perspective.
     *
     * @return current {@link DateTime#now()}  calculated using actually globally set {@link com.sabre.ssse.common.time.Clock}
     */
    public static DateTime now()
    {
        return getClock().getTime();
    }

    /**
     * This method always provides current system time and time zone is default system time zone.
     *
     * <p>This method should be used to get current time when you want to have it guaranteed
     * that this time is real system time not mocked
     *
     * @return always real system current localDateTime using {@link com.sabre.ssse.common.time.RealSystemClock}
     */
    public static DateTime realTime()
    {
        return RealSystemClock.getInstance().getTime();
    }
}
