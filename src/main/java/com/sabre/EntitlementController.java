package com.sabre;

import com.sabre.model.Entitlement;
import com.sabre.model.PromoCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sabre.service.EntitlementService;

import java.util.List;

@RestController
@RequestMapping("/promo")
public class EntitlementController
{
    @Autowired
    private EntitlementService entitlementService;

    @RequestMapping(value = "/entitlements", method = RequestMethod.GET)
    public List<Entitlement> message(@RequestParam(value = "location", required = false) String location)
    {
        return entitlementService.getEntitlements(location);
    }

    @RequestMapping(value = "/generatePromoCode", method = RequestMethod.POST)
    public String generatePromoCode(@RequestParam(value = "offeringId", required = true) Long offeringId)
    {
        return entitlementService.generatePromoCode(offeringId);
    }

    @RequestMapping(value = "/promoCode/{promoCode}", method = RequestMethod.GET)
    public PromoCode promoCode(@PathVariable(value = "promoCode") String promoCode)
    {
        return entitlementService.getPromoCode(promoCode);
    }

}
