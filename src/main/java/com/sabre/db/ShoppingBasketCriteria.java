/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;

public class ShoppingBasketCriteria implements Serializable
{
    private static final int NUM_ITINERARY_PARTS_ONEWAY = 1;
    private static final int NUM_ITINERARY_PARTS_ROUNDTRIP = 2;
    private static final int NUM_ITINERARY_PARTS_MULTICITY = 3;


    private Long id;

    // fare
    private FieldCriteriaString fareBasis = new FieldCriteriaString();
    private FieldCriteriaString bookingClass = new FieldCriteriaString();
    private FieldCriteriaString bookingClassCategory = new FieldCriteriaString();
    private FieldCriteriaString fareClassName = new FieldCriteriaString();
    private FieldCriteriaString privateFareCodes = new FieldCriteriaString();
    private FieldCriteriaString fareType = new FieldCriteriaString();
    private FieldCriteriaString tierLevel = new FieldCriteriaString();
    private FieldCriteriaString brandId = new FieldCriteriaString();

    // Traveler
    private FieldCriteriaString highestPassengersTierLevel = new FieldCriteriaString();

    // search
    private FieldCriteriaString paxType = new FieldCriteriaString();
    private FieldCriteriaString language = new FieldCriteriaString();
    private FieldCriteriaString countryOfResidence = new FieldCriteriaString();
    private Integer numberOfItineraryParts = null;
    private FieldCriteriaString pointOfSale = new FieldCriteriaString();

    // itinerary
    private FieldCriteriaLocation origin = new FieldCriteriaLocation();
    private FieldCriteriaLocation destination = new FieldCriteriaLocation();
    private FieldCriteriaString operatingAirline = new FieldCriteriaString();
    private FieldCriteriaFlightNumberString flightNumbers = new FieldCriteriaFlightNumberString();
    private FieldCriteriaDate departureDate = new FieldCriteriaDate();
    private FieldCriteriaDate returnDate = new FieldCriteriaDate();
    private FieldCriteriaString routeCategory = new FieldCriteriaString();
    private Long minDepartureTime = null;

    // shopping
    private FieldCriteriaDate bookingDate = new FieldCriteriaDate();
    private FieldCriteriaString pricingCurrency = new FieldCriteriaString();
    private FieldCriteriaString paymentTypes = new FieldCriteriaString();

    private FieldCriteriaString paymentMethod = new FieldCriteriaString();

    private FieldCriteriaString promoCode = new FieldCriteriaString();

    private UserCriteria bookerCriteria = new UserCriteria();

    private boolean notModifiedOffline = false;

    // entitlements/products
    private FieldCriteriaString appliedEntitlements = new FieldCriteriaString();


    public boolean isBrandIdDefined()
    {
        return this.brandId.isDefined();
    }

    public boolean isBrandIdValid(String brandId)
    {
        return this.brandId.isValid(brandId);
    }

    public boolean isPassengersHighestTierLevelValid(String tierLevel)
    {
        return this.highestPassengersTierLevel.isValid(tierLevel);
    }


    /**
     * @return field
     */
    public FieldCriteriaString getBookingClass()
    {
        return bookingClass;
    }

    public void setBookingClass(FieldCriteriaString bookingClass)
    {
        this.bookingClass = bookingClass;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getBookingClassCategory()
    {
        return bookingClassCategory;
    }

    public void setBookingClassCategory(FieldCriteriaString bookingClassCategory)
    {
        this.bookingClassCategory = bookingClassCategory;
    }

    /**
     * @return field
     */
    public FieldCriteriaLocation getDestination()
    {
        return destination;
    }

    public void setDestination(FieldCriteriaLocation destination)
    {
        this.destination = destination;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getFareBasis()
    {
        return fareBasis;
    }

    public void setFareBasis(FieldCriteriaString fareBasis)
    {
        this.fareBasis = fareBasis;
    }

    /**
     * @return field
     */
    public FieldCriteriaLocation getOrigin()
    {
        return origin;
    }

    public void setOrigin(FieldCriteriaLocation origin)
    {
        this.origin = origin;
    }

    /**
     * Gets the unique ID of this instance.
     * 
     * @return A Long that uniquely identifies this instance.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Sets the unique ID of this instance
     * 
     * @param id
     *            id
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return field
     */
    public FieldCriteriaDate getBookingDate()
    {
        return bookingDate;
    }

    public void setBookingDate(FieldCriteriaDate bookingDate)
    {
        this.bookingDate = bookingDate;
    }

    /**
     * will use creditCardTypes field in the DB this includes now ATM type and Cash type prefixes
     * 
     * 
     * @return field
     */
    public FieldCriteriaString getPaymentTypes()
    {
        return paymentTypes;
    }

    public void setPaymentTypes(FieldCriteriaString paymentTypes)
    {
        this.paymentTypes = paymentTypes;
    }

    /**
     * @return field
     */
    public FieldCriteriaDate getDepartureDate()
    {
        return departureDate;
    }

    public void setDepartureDate(FieldCriteriaDate departureDate)
    {
        this.departureDate = departureDate;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getFareClassName()
    {
        return fareClassName;
    }

    public void setFareClassName(FieldCriteriaString fareClassName)
    {
        this.fareClassName = fareClassName;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getPrivateFareCodes()
    {
        return privateFareCodes;
    }

    public void setPrivateFareCodes(FieldCriteriaString privateFareCodes)
    {
        this.privateFareCodes = privateFareCodes;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getFareType()
    {
        return fareType;
    }

    public void setFareType(FieldCriteriaString fareType)
    {
        this.fareType = fareType;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getTierLevel()
    {
        return tierLevel;
    }

    public void setTierLevel(FieldCriteriaString frequentFlyerTierLevel)
    {
        this.tierLevel = frequentFlyerTierLevel;
    }

    public FieldCriteriaString getHighestPassengersTierLevel()
    {
        return highestPassengersTierLevel;
    }

    public void setHighestPassengersTierLevel(FieldCriteriaString highestPassengersTierLevel)
    {
        this.highestPassengersTierLevel = highestPassengersTierLevel;
    }

    public FieldCriteriaString getBrandId()
    {
        return brandId;
    }

    public void setBrandId(FieldCriteriaString brandId)
    {
        this.brandId = brandId;
    }

    /**
     * @return field
     */
    public FieldCriteriaFlightNumberString getFlightNumbers()
    {
        return flightNumbers;
    }

    public void setFlightNumbers(FieldCriteriaFlightNumberString flightNumbers)
    {
        this.flightNumbers = flightNumbers;
    }

    /**
     * @return minimum departure time
     */
    public Long getMinDepartureTime()
    {
        return minDepartureTime;
    }

    public void setMinDepartureTime(Long minDepartureTime)
    {
        this.minDepartureTime = minDepartureTime;
    }

    /**
     * @return number of itinerary parts
     */
    public Integer getNumberOfItineraryParts()
    {
        return numberOfItineraryParts;
    }

    public void setNumberOfItineraryParts(Integer numberOfItineraryParts)
    {
        this.numberOfItineraryParts = numberOfItineraryParts;
    }


    public boolean itineraryPartsRuleIsOneway()
    {
        if (numberOfItineraryParts != -1 && numberOfItineraryParts.toString().contains(String.valueOf(NUM_ITINERARY_PARTS_ONEWAY)))
        {
            return true;
        }
        return false;
    }

    public boolean itineraryPartsRuleIsRoundtrip()
    {
        if (numberOfItineraryParts.toString().contains(String.valueOf(NUM_ITINERARY_PARTS_ROUNDTRIP)))
        {
            return true;
        }

        return false;
    }

    public boolean itineraryPartsRuleIsMulticity()
    {
        if (numberOfItineraryParts.toString().contains(String.valueOf(NUM_ITINERARY_PARTS_MULTICITY)))
        {
            return true;
        }

        return numberOfItineraryParts == NUM_ITINERARY_PARTS_MULTICITY;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getOperatingAirline()
    {
        return operatingAirline;
    }

    public void setOperatingAirline(FieldCriteriaString operatingAirline)
    {
        this.operatingAirline = operatingAirline;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getRouteCategory()
    {
        return routeCategory;
    }

    public void setRouteCategory(FieldCriteriaString routeCategory)
    {
        this.routeCategory = routeCategory;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getPaxType()
    {
        return paxType;
    }

    public void setPaxType(FieldCriteriaString paxType)
    {
        this.paxType = paxType;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getPricingCurrency()
    {
        return pricingCurrency;
    }

    public void setPricingCurrency(FieldCriteriaString pricingCurrency)
    {
        this.pricingCurrency = pricingCurrency;
    }

    /**
     * @return field
     */
    public FieldCriteriaDate getReturnDate()
    {
        return returnDate;
    }

    public void setReturnDate(FieldCriteriaDate returnDate)
    {
        this.returnDate = returnDate;
    }


    /**
     * @return field
     */
    public FieldCriteriaString getCountryOfResidence()
    {
        return countryOfResidence;
    }

    public void setCountryOfResidence(FieldCriteriaString countryOfResidence)
    {
        this.countryOfResidence = countryOfResidence;
    }


    /**
     * @return field
     */
    public UserCriteria getBookerCriteria()
    {
        return bookerCriteria;
    }

    public void setBookerCriteria(UserCriteria bookerCriteria)
    {
        this.bookerCriteria = bookerCriteria;
    }


    /**
     * @return A FieldCriteriaString allowed/disallowed Points Of Sale
     */
    public FieldCriteriaString getPointOfSale()
    {
        return pointOfSale;
    }

    public void setPointOfSale(FieldCriteriaString pointOfSale)
    {
        this.pointOfSale = pointOfSale;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getAppliedEntitlements()
    {
        return appliedEntitlements;
    }

    public void setAppliedEntitlements(FieldCriteriaString appliedEntitlements)
    {
        this.appliedEntitlements = appliedEntitlements;
    }


    public boolean hasFareCriteria()
    {
        return (getFareBasis().isDefined() || getBookingClass().isDefined() || getBookingClassCategory().isDefined() || getFareType().isDefined() || getPrivateFareCodes().isDefined() || getBrandId().isDefined());
    }

    /**
     * @return field
     */
    public FieldCriteriaString getPromoCode()
    {
        return promoCode;
    }

    public void setPromoCode(FieldCriteriaString promoCode)
    {
        this.promoCode = promoCode;
    }

    public boolean isNotModifiedOffline()
    {
        return notModifiedOffline;
    }

    public void setNotModifiedOffline(boolean notModifiedOffline)
    {
        this.notModifiedOffline = notModifiedOffline;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getLanguage()
    {
        return language;
    }

    public void setLanguage(FieldCriteriaString language)
    {
        this.language = language;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getPaymentMethod()
    {
        return paymentMethod;
    }

    public void setPaymentMethod(FieldCriteriaString paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }


}
