package com.sabre.db;

import org.joda.time.DateTime;

/**
 * This implementation of {@link com.sabre.ssse.common.time.Clock}  delegates to configured
 * per thread {@link com.sabre.ssse.common.time.Clock} implementation.
 *
 * <p>If there is not configured {@link com.sabre.ssse.common.time.Clock} for given thread
 * it uses {@link com.sabre.ssse.common.time.RealSystemClock}
 */
public class ConfigurableThreadLocalClock implements Clock
{
    private static final ConfigurableThreadLocalClock INSTANCE = new ConfigurableThreadLocalClock();

    private ConfigurableThreadLocalClock()
    {
    }

    /**
     * @return instance of {@link com.sabre.ssse.common.time.ConfigurableThreadLocalClock}
     */
    public static ConfigurableThreadLocalClock getInstance()
    {
        return INSTANCE;
    }

    private final ThreadLocal<Clock> clockThreadLocal = new ThreadLocal<Clock>()
    {
        @Override
        protected Clock initialValue()
        {
            return RealSystemClock.getInstance();
        }
    };

    /**
     * Sets given {@link Clock} instance to be used as delegate in thread calling this method
     * @param clock - given clock instance
     */
    public final void registerClock(Clock clock)
    {
        clockThreadLocal.set(clock);
    }

    /**
     * Sets default {@link Clock} implementation  instance to be used as delegate in  thread calling this method
     */
    public final void unregisterClock()
    {
        clockThreadLocal.set(RealSystemClock.getInstance());
    }

    /**
     * Gets  Clock implementation  instance that is set to be used as delegate in the thread calling this method
     */
    public final Clock getRegisteredClock()
    {
        return clockThreadLocal.get();
    }

    /**
     * This returns {@link DateTime} according to implementation of delegated {@link com.sabre.ssse.common.time.Clock}
     *  for thread that this method is called from
     * @return current time depended on {@link Clock} set
     */
    @Override
    public DateTime getTime()
    {
        return getRegisteredClock().getTime();
    }

}
