package com.pesikovlike.kalah.model.dao.interfaces;

import com.pesikovlike.kalah.model.entity.User;

import javax.ejb.Local;

/**
 * Created by Igor on 31.10.2016.
 */
@Local
public interface UserDAO {
    public void insertUser(User user);

    public void updateUser(User user);

    public void deleteUser(User user);

    public User getUserById(long id);

    public User getUserByLogin(String login);
}
