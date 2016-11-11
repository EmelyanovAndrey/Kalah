package com.pesikovlike.kalah.model.dao;

import com.pesikovlike.kalah.model.dao.interfaces.*;
import com.pesikovlike.kalah.model.dao.postgres.hibernate.PostgresHibernateAvatarDAO;
import com.pesikovlike.kalah.model.dao.postgres.hibernate.PostgresHibernateGameStateDAO;
import com.pesikovlike.kalah.model.dao.postgres.hibernate.PostgresHibernateHoleDAO;
import com.pesikovlike.kalah.model.dao.postgres.hibernate.PostgresHibernateUserDAO;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Created by Igor on 01.11.2016.
 */
public class DAOFactory {

    private AvatarDAO avatarDAO;
    private UserDAO userDAO;
    private GameStateDAO gameStateDAO;
    private HoleDAO holeDAO;


    public enum DataSources {POSTGRES_HIBERNATE}

    private static DataSources dataSource = null;


    public static void init(DataSources dataSource) {
        DAOFactory.dataSource = dataSource;
    }

    @Produces
    @ApplicationScoped
    @Named("avatarDAO")
    public AvatarDAO getAvatarDAO() {
        if (avatarDAO == null)
            switch (dataSource) {
                case POSTGRES_HIBERNATE:
                    avatarDAO = new PostgresHibernateAvatarDAO();
            }
        return avatarDAO;
    }

    @Produces
    @ApplicationScoped
    @Named("userDAO")
    public UserDAO getUserDAO() {
        if(userDAO == null)
        switch (dataSource) {
            case POSTGRES_HIBERNATE:
                userDAO = new PostgresHibernateUserDAO();
        }
        return userDAO;
    }

    @Produces
    @ApplicationScoped
    @Named("gameStateDAO")
    public GameStateDAO getGameStateDAO() {
        if(gameStateDAO == null)
        switch (dataSource) {
            case POSTGRES_HIBERNATE:
                gameStateDAO = new PostgresHibernateGameStateDAO();
        }
        return gameStateDAO;
    }

    @Produces
    @ApplicationScoped
    @Named("holeDAO")
    public HoleDAO getHoleDAO() {
        if(holeDAO == null)
        switch (dataSource) {
            case POSTGRES_HIBERNATE:
                holeDAO = new PostgresHibernateHoleDAO();
        }
        return holeDAO;
    }
}
