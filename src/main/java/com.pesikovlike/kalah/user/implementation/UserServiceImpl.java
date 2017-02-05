package com.pesikovlike.kalah.user.implementation;

import com.pesikovlike.kalah.model.dao.AvatarDAO;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.Avatar;
import com.pesikovlike.kalah.model.entity.User;
import com.pesikovlike.kalah.user.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor on 06.11.2016.
 */
@Stateless
public class UserServiceImpl implements UserService {
    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    @Inject
    @Named("avatarDAO")
    private AvatarDAO avatarDAO;

    private static final Logger LOGGER = Logger.getLogger("User Service");

    public boolean userExist(String login) {
        User user = userDAO.getUserByLogin(login);
        return user == null ? false : true;
    }

    public int register(String login, String password, String email, int avatarId) {
        if (userExist(login)) {
            return 1; //пользователь уже существует
        }
        Avatar avatar = avatarDAO.getAvatarById(avatarId);
        LOGGER.log(Level.SEVERE, avatar.getAvatarName());
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setAvatar(avatar);
        userDAO.insertUser(user);

        return 0;
    }

    public int authorize(String login, String password) {
        if (!userExist(login)) {
            return 1; //пользователя не существует
        }
        User user = userDAO.getUserByLogin(login);
        if(!user.getPassword().equals(password)) {
            return 2; //пароли не совпадают
        }

        //код авторизации
        return 0;
    }
}
