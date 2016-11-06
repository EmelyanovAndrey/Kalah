package com.pesikovlike.kalah.server;


import javax.annotation.PostConstruct;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;


/**
 * Created by Igor on 06.11.2016.
 */
@Singleton
@Startup
public class Bootstrapssss {

    @Inject
    private PostgresHibernateStartup postgresHibernateStartup;

    @PostConstruct
    public void postConstruct(){
        postgresHibernateStartup.init();
    }


}
