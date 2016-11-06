package com.pesikovlike.kalah.model.dao;

import com.pesikovlike.kalah.model.dao.interfaces.DAOFactory;
import com.pesikovlike.kalah.model.dao.postgres.hibernate.PostgresHibernateFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;


/**
 * Created by Igor on 31.10.2016.
 */
public class InitFactory {

    public enum DataSources {POSTGRES_HIBERNATE}

    private static DataSources dataSource;

    private DAOFactory factory;

    public static void init(DataSources dataSource){
        InitFactory.dataSource = dataSource;
    }

    @Produces
    @RequestScoped
    @Named("daoFactory")
    public DAOFactory getDAOFactory() {

        if(dataSource == null){
            switch (dataSource) {
                case POSTGRES_HIBERNATE:
                    factory = new PostgresHibernateFactory();
                default:
                    return null;
            }
        }
        return factory;
    }
}
