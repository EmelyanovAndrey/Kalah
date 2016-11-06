package com.pesikovlike.kalah.model.dao.interfaces;

import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.User;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Igor on 31.10.2016.
 */
@Local
public interface GameStateDAO {
    public void insertGameState(GameState gameState);

    public void updateGameState(GameState gameState);

    public void deleteGameState(GameState gameState);

    public GameState getGameStateById(long id);

    public List<GameState> getGameStatesByUser(User user);
}
