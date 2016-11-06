package com.pesikovlike.kalah.model.hibernate;

import org.hibernate.cfg.Configuration;

/**
 * Created by Igor on 30.10.2016.
 */


public class PostgresHibernateSessionFactory {

    private static final org.hibernate.SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure("/postgres/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    /**
     * Получение фабрики сессий
     * @return фабрика сессий
     */
    public static org.hibernate.SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
