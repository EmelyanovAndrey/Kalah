package com.pesikovlike.kalah.user;

import com.pesikovlike.kalah.model.entity.GameState;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Igor on 06.12.2016.
 */
@Local
public interface UserService {
    public boolean userExist(String login);

    public int register(String login, String password, String email, int avatarId);

    public int authorize(String login, String password);

    public int change(String oldLogin, String login, String password, String email);

    public List<GameState> getSavedGamesForUser(String login);
}
