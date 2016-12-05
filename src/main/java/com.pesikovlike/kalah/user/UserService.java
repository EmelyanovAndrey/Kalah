package com.pesikovlike.kalah.user;

import javax.ejb.Local;

/**
 * Created by Igor on 06.12.2016.
 */
@Local
public interface UserService {
    public boolean userExist(String login);

    public int register(String login, String password, String email, int avatarId);

    public int authorize(String login, String password);
}
