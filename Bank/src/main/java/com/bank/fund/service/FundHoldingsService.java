package com.bank.fund.service;


import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.fund.entity.FundHoldings;

@Service
@Transactional
public class FundHoldingsService {

    @Autowired
    private SessionFactory sessionFactory;

    public boolean insert(FundHoldings bean) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.persist(bean);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean update(FundHoldings bean) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.merge(bean);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(FundHoldings id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            FundHoldings entity = session.find(FundHoldings.class, id);
            if (entity != null) {
                session.remove(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
