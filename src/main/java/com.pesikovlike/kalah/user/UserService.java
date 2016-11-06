package com.pesikovlike.kalah.user;

import com.pesikovlike.kalah.model.dao.interfaces.AvatarDAO;
import com.pesikovlike.kalah.model.dao.interfaces.DAOFactory;
import com.pesikovlike.kalah.model.dao.interfaces.UserDAO;
import com.pesikovlike.kalah.model.entity.Avatar;
import com.pesikovlike.kalah.model.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Igor on 06.11.2016.
 */
@Stateless
public class UserService {

    @Inject
    @Named("daoFactory")
    private DAOFactory daoFactory;

    private boolean userExist(String login) {
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = userDAO.getUserByLogin(login);
        return user == null ? false : true;
    }

    public int register(String login, String password, String email, int avatarId) {
        if (userExist(login)) {
            return 1;
        }
        AvatarDAO avatarDAO = daoFactory.getAvatarDAO();
        Avatar avatar = avatarDAO.getAvatarById(avatarId);
        User user = new User(login, password, email, avatar);
        UserDAO userDAO = daoFactory.getUserDAO();
        userDAO.insertUser(user);

        return 0;
    }

    public int authorize(String login, String password) {
        if (!userExist(login)) {
            return 1;
        }
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = userDAO.getUserByLogin(login);
        if(!user.getPassword().equals(password)) {
            return 2;
        }

        return 0;
    }
}
