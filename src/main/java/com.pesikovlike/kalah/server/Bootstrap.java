package com.pesikovlike.kalah.server;


import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.session.GameSessionFactory;

import javax.annotation.PostConstruct;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;


/**
 * Created by Igor on 06.11.2016.
 */
@Singleton
@Startup
public class Bootstrap {

    @Inject
    private PostgresHibernateStartup postgresHibernateStartup;

    @PostConstruct
    public void postConstruct(){
        postgresHibernateStartup.init();
        GameSessionFactory.init(GameSessionFactory.GameSessionImpl.IMPL);
        GameBidFactory.init(GameBidFactory.GameBidImpl.IMPL);
    }


}
