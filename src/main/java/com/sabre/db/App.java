package com.sabre.db;

import java.util.List;

import com.sabre.service.EntitlementServiceDBImpl;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class App {
    public static void main(String[] args) {
        System.out.println("Maven + Hibernate + Oracle");
        Session session = HibernateUtil.getSessionFactory().openSession();
//
        session.beginTransaction();
        //Redemption redemption = new Redemption();

        //session.createQuery("from Offering ").list();

        //List<ShoppingBasketCriteria> list = session.createQuery("from ShoppingBasketCriteria ").list();

        //System.out.println("@@@@@@@@@@@@@@@" + list.get(0).getFlightNumbers());

       // List<com.sabre.model.Entitlement> entitlements = new EntitlementServiceDBImpl().getEntitlements("");
      //  System.out.println("@@@@@@@@@@@@@@@");
        //session.get(Redemption.class, 1000032370);

        //

//                PromoCodes promoCode = new PromoCodes();
//        promoCode.setOfferingName("50U0610131");
//        promoCode.setPromoCode("99468944");
//
//        session.save(promoCode);

        org.hibernate.Criteria criteria = session.createCriteria(PromoCodes.class);

        criteria.add(Restrictions.eq("offeringName", "50U0610131"));
        List list = criteria.list();

        session.getTransaction().commit();
    }
}
