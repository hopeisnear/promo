package com.sabre.service;

import com.sabre.model.Entitlement;
import com.sabre.model.PromoCode;

import java.util.List;

public interface EntitlementService
{
    List<Entitlement> getEntitlements(String location);

    String generatePromoCode(Long offeringId);

    PromoCode getPromoCode(String promoCode);

}
