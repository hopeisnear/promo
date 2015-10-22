/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;

/**
 * Utility class used by {@link OfferingCoupon} to generate redemption codes.
 */
public class RedemptionCodeService
{
    private static final Logger log = LoggerFactory.getLogger(RedemptionCodeService.class);

    private final static Map<RedemptionCodesCacheKey, Collection<String>> redemptionCodesCache = new ConcurrentHashMap<RedemptionCodesCacheKey, Collection<String>>();

    private static final Timer clearCacheTimer = new Timer(true);
    private static final TimerTask clearCacheTask = new RedemptionCodeCacheTimerTask();

    private static final String[] redemptionCodePadding = { "I", "K", "M", "S", "V", "R", "H", "L", "W" };

    private RedemptionCodeService()
    {
    }

    static
    {
        // clears cache every 1 hour
        clearCacheTimer.schedule(clearCacheTask, 3600000, 3600000);
    }

    public static Collection<String> generateRedemptionCodes(String prefix, String encryptionKey, int quantity)
    {
        if (StringUtils.isEmpty(encryptionKey))
        {
            log.error("Redemption codes for offeringCoupon with prefix '" + prefix + "' cannot be generated without an encryption key.");
        }

        RedemptionCodesCacheKey redemptionCodesCacheKey = new RedemptionCodesCacheKey(prefix, encryptionKey, quantity);

        Collection<String> redemptionCodes = redemptionCodesCache.get(redemptionCodesCacheKey);
        if (redemptionCodes == null)
        {
            redemptionCodes = insertRedemptionCodes(prefix, encryptionKey, quantity, redemptionCodesCacheKey);
        }

        return Collections.unmodifiableCollection(redemptionCodes);
    }

    public static List<String> generateRedemptionCodes(String prefix, String key, int quantity, Collection<String> skippedCodes)
    {
        return generateEncryptedRedemptionCodes(prefix, key, quantity, skippedCodes);
    }

    public static boolean isValidSingleUseRedemptionCode(String redemptionCode, String encryptionKey, int quantity)
    {
        if (StringUtils.isEmpty(encryptionKey))
        {
            throw new IllegalStateException("Redemption codes cannot be validated without an encryption key.");
        }

        if (redemptionCode.length() < OfferingCoupon.REDEMPTION_CODE_GENERATED_CHARS)
        {
            return false;
        }

        String redemptionCodePrefix = redemptionCode.substring(0, redemptionCode.length() - OfferingCoupon.REDEMPTION_CODE_GENERATED_CHARS);

        return generateRedemptionCodes(redemptionCodePrefix, encryptionKey, quantity).contains(redemptionCode);
    }

    protected static List<String> generateEncryptedRedemptionCodes(String prefix, String key, int quantity, Collection<String> skippedCodes)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            CRC32 crc32 = new CRC32();

            List<String> redemptionCodes = new ArrayList<String>(quantity);
            int counter = 0;

            while (redemptionCodes.size() < quantity)
            {
                byte[] digest = md.digest((key + counter++).getBytes());
                crc32.reset();
                crc32.update(digest);
                String redemptionCode = Long.toHexString(crc32.getValue()).toUpperCase();

                int paddingCount = 0;
                StringBuffer buf = new StringBuffer();
                buf.append(redemptionCode);

                while (buf.length() < OfferingCoupon.REDEMPTION_CODE_GENERATED_CHARS)
                {
                    if (buf.length() < paddingCount)
                    {
                        paddingCount = 0;
                    }
                    buf.append(redemptionCodePadding[paddingCount++]);

                }

                redemptionCode = buf.toString();
                String code = prefix != null ? prefix + redemptionCode : redemptionCode;

                if (skippedCodes != null && !skippedCodes.isEmpty() && skippedCodes.contains(code))
                {
                    continue;
                }

                redemptionCodes.add(code);
            }

            return redemptionCodes;

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Collection<String> insertRedemptionCodes(String prefix, String key, int quantity, RedemptionCodesCacheKey redemptionCodesCacheKey)
    {
        Collection<String> redemptionCodes = generateEncryptedRedemptionCodes(prefix, key, quantity, Collections.EMPTY_LIST);

        redemptionCodesCache.put(redemptionCodesCacheKey, redemptionCodes);

        return redemptionCodes;
    }

    private static void clearCache()
    {
        if (!redemptionCodesCache.isEmpty())
        {
            redemptionCodesCache.clear();
            log.debug("Redemption Codes Cache Cleared");
        }
    }

    static class RedemptionCodesCacheKey
    {
        private String key;
        private int quantity;
        private String prefix;

        public RedemptionCodesCacheKey(String prefix, String key, int quantity)
        {
            super();
            this.key = key;
            this.quantity = quantity;
            this.prefix = prefix;
        }

        @Override
        public boolean equals(Object other)
        {
            if (!(other instanceof RedemptionCodesCacheKey))
            {
                return false;
            }

            RedemptionCodesCacheKey otherRedemptionCodeCache = (RedemptionCodesCacheKey) other;

            return this.key.equals(otherRedemptionCodeCache.key) &&
                    this.quantity == otherRedemptionCodeCache.quantity &&
                    safeEquals(this.prefix, otherRedemptionCodeCache.prefix);
        }

        @Override
        public int hashCode()
        {
            return this.key.hashCode() + quantity + (this.prefix != null ? this.prefix.hashCode() : 0) + 17;

        }
    }

    private static boolean safeEquals(String first, String second)
    {
        if (StringUtils.isEmpty(first) && StringUtils.isEmpty(second))
        {
            return true;
        }
        if (StringUtils.isEmpty(first) && !StringUtils.isEmpty(second))
        {
            return false;
        }
        if (!StringUtils.isEmpty(first) && StringUtils.isEmpty(second))
        {
            return false;
        }
        return first.equals(second);
    }

    private static class RedemptionCodeCacheTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            clearCache();
        }
    }
}
