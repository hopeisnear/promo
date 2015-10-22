/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

public enum OfferingCouponType
{
    // Each Coupon has a unique code and can only be used once
    SINGLEUSE, // SINGLEUSE per pnr
    SINGLEUSE_PER_PAX,
    // Only one coupon code. It can be reused
    MULTIUSE,  // MULTIUSE per pnr
    MULTIUSE_PER_PAX,
    // Not stored - used in UI
    CONTINUAL
}
