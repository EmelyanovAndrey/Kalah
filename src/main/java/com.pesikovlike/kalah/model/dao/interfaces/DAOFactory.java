package com.pesikovlike.kalah.model.dao.interfaces;

import javax.ejb.Local;

/**
 * Created by Igor on 06.11.2016.
 */
@Local
public interface DAOFactory {

    public AvatarDAO getAvatarDAO();

    public UserDAO getUserDAO();

    public GameStateDAO getGameStateDAO();

    public HoleDAO getHoleDAO();

}
