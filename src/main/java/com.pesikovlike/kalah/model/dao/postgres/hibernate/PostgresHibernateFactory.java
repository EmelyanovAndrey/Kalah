package com.pesikovlike.kalah.model.dao.postgres.hibernate;

import com.pesikovlike.kalah.model.dao.interfaces.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by Igor on 01.11.2016.
 */
@Stateless
public class PostgresHibernateFactory implements DAOFactory {

    @EJB
    private AvatarDAO avatarDAO;
    @EJB
    private UserDAO userDAO;
    @EJB
    private GameStateDAO gameStateDAO;
    @EJB
    private HoleDAO holeDAO;


    public AvatarDAO getAvatarDAO() {
        return avatarDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public GameStateDAO getGameStateDAO() {
        return gameStateDAO;
    }

    public HoleDAO getHoleDAO() {
        return holeDAO;
    }
}
