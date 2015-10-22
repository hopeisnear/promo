package com.sabre.db;

import org.joda.time.DateTime;

/**
 * This interface provides access to current time.
 *
 * <p>This gives possibility to mock current time if needed.
 */
public interface Clock
{
    /**
     * Provides current time.
     *
     * <p>Time zone depends on the implementation and should be documented in the implementing class.
     *
     * @return current time (now) {@link DateTime}
     */
    DateTime getTime();
}
