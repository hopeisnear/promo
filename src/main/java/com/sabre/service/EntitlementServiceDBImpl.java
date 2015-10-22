package com.sabre.service;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.sabre.db.FieldCriteriaDate;
import com.sabre.db.PriceModifier;
import com.sabre.db.PromoCodes;
import com.sabre.model.Entitlement;
import com.sabre.db.HibernateUtil;
import com.sabre.db.Offering;
import com.sabre.db.OfferingCoupon;
import com.sabre.db.Redemption;
import com.sabre.db.ShoppingBasketCriteria;
import com.sabre.model.PromoCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class EntitlementServiceDBImpl implements EntitlementService
{

    @Override
    public List<Entitlement> getEntitlements(final String location)
    {
        Session session = openDBSession();

        List<Offering> offerings = session.createQuery("from Offering ").list();
        ImmutableList.Builder<Entitlement> entitlementBuilder = ImmutableList.<Entitlement>builder();

        for (final Offering offering : offerings)
        {

            if(offering.getOfferingCoupon() == null)
            {
                continue;
            }

            Entitlement entitlement = new Entitlement();

            ShoppingBasketCriteria shoppingBasketCriteria = offering.getShoppingBasketCriteria2();
            entitlement.setFlightNumber(extractFlightNumber(shoppingBasketCriteria));

            entitlement.setValidFrom(extractStartDate(shoppingBasketCriteria.getDepartureDate()));
            entitlement.setValidTo(extractEndDate(shoppingBasketCriteria.getDepartureDate()));

            entitlement.setOrigin(extractOriginAirportCode(offering));
            entitlement.setDestination(extractDestinationAirportCode(offering));

            if(offering.getOfferingCoupon() != null)
            {
                entitlement.setUsedCount(offering.getOfferingCoupon().getUsedCount());
                entitlement.setQuantity(offering.getOfferingCoupon().getQuantity());
            }

            entitlement.setOfferingId(offering.getId());

            PriceModifier priceModifier = (PriceModifier)session.createCriteria(PriceModifier.class).add(Restrictions.eq("id", offering.getId().intValue())).uniqueResult();
            entitlement.setDiscount(priceModifier.getPercentageAmount());

            entitlementBuilder.add(entitlement);
        }

        closeSession(session);

        List<Entitlement> entitlements = entitlementBuilder.build();

        if(StringUtils.isNotEmpty(location))
        {
            return ImmutableList.copyOf(Iterables.filter(entitlements, new Predicate<Entitlement>()
            {
                @Override public boolean apply(Entitlement entitlement)
                {
                    return entitlement.getOrigin().equalsIgnoreCase(location);
                }
            }));
        }
        else
        {
            return entitlements;
        }

    }

    @Override public String generatePromoCode(Long offeringId)
    {
        Session session = openDBSession();
        Offering offering = (Offering)session.get(Offering.class, offeringId);
        List<Redemption> redemptions = getRedemptions(session, offering);
        List<PromoCodes> promoCodes = session.createCriteria(PromoCodes.class).add(Restrictions.eq("offeringName", "50U0610131")).list();
        List<String> usedCodes = getUsedCodes(offering, redemptions, promoCodes);

        PromoCodes promoCode = new PromoCodes();
        promoCode.setOfferingName(offering.getName());
        String promoCode1 = usedCodes.get(0);
        promoCode.setPromoCode(promoCode1);
        session.save(promoCode);
        session.getTransaction().commit();
        session.close();
        return promoCode1;
    }

    @Override public PromoCode getPromoCode(final String promoCode)
    {
        Session session = openDBSession();
        List<Redemption> redemptions = session.createQuery("from Redemption ").list();

        Optional<Redemption> redemptionOptional = Iterables.tryFind(redemptions, new Predicate<Redemption>()
        {
            @Override public boolean apply(Redemption redemption)
            {
                return redemption.getCode().equals(promoCode);
            }
        });

        boolean isValid = !(redemptionOptional.isPresent());
        PromoCode promo = new PromoCode();
        promo.setCode(promoCode);
        promo.setIsValid(isValid);
        return promo;
    }

    private String extractOriginAirportCode(Offering offering)
    {
        if (offering.getShoppingBasketCriteria2().getOrigin() != null && !CollectionUtils.isEmpty(offering.getShoppingBasketCriteria2().getOrigin().getIncludes()) && Iterables.get(offering.getShoppingBasketCriteria2().getOrigin().getIncludes(), 0) != null)
        {
            return Iterables.get(offering.getShoppingBasketCriteria2().getOrigin().getIncludes(), 0).getCode();
        }
        return null;
    }

    private String extractDestinationAirportCode(Offering offering)
    {
        if (offering.getShoppingBasketCriteria2().getDestination() != null && !CollectionUtils.isEmpty(offering.getShoppingBasketCriteria2().getDestination().getIncludes()) && Iterables.get(offering.getShoppingBasketCriteria2().getDestination().getIncludes(), 0) != null)
        {
            return Iterables.get(offering.getShoppingBasketCriteria2().getDestination().getIncludes(), 0).getCode();
        }
        return null;
    }

    private List<String> getUsedCodes(Offering offering, List<Redemption> redemptions, List<PromoCodes> promoCodes)
    {

        Iterable<String> redemptionUsedCodes = Iterables.transform(redemptions, new Function<Redemption, String>()
        {
            @Override public String apply(Redemption redemption)
            {
                return redemption.getCode();
            }
        });

        Iterable<String> usedPromoCodes = Iterables.transform(promoCodes, new Function<PromoCodes, String>()
        {
            @Override public String apply(PromoCodes promoCodes)
            {
                return promoCodes.getPromoCode();
            }
        });

        Set<String> usedCodes = ImmutableSet.<String>builder().addAll(redemptionUsedCodes).addAll(usedPromoCodes).build();

        OfferingCoupon offeringCoupon = offering.getOfferingCoupon();
        return offeringCoupon.generateRemainingCouponCodes(usedCodes, false);
    }

    private List<Redemption> getRedemptions(Session session, final Offering offering)
    {
        List<Redemption> redemptions = session.createQuery("from Redemption ").list();
        redemptions = ImmutableList.copyOf(Iterables.filter(redemptions, new Predicate<Redemption>()
        {
            @Override
            public boolean apply(Redemption redemption)
            {
                return redemption.getOffering().getId().equals(offering.getId());
            }
        }));
        return redemptions;
    }

    private Date extractStartDate(FieldCriteriaDate fieldCriteriaDate)
    {
        if (fieldCriteriaDate != null&& fieldCriteriaDate.getDate1() != null)
        {
            return fieldCriteriaDate.getDate1().getStart();
        }
        return null;
    }


    private Date extractEndDate(FieldCriteriaDate fieldCriteriaDate)
    {
        if (fieldCriteriaDate != null&& fieldCriteriaDate.getDate1() != null)
        {
            return fieldCriteriaDate.getDate1().getEnd();
        }
        return null;
    }

    private String extractFlightNumber(ShoppingBasketCriteria shoppingBasketCriteria)
    {
        Collection<String> flightNumbers = shoppingBasketCriteria.getFlightNumbers().getIncludes();
        if (!CollectionUtils.isEmpty(flightNumbers))
        {
            return Iterables.get(flightNumbers, 0);
        }
        return null;
    }

    private Session openDBSession()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        return session;
    }

    private void closeSession(Session session)
    {
        session.close();
    }
}
