package com.pesikovlike.kalah.game.session;

import com.pesikovlike.kalah.game.session.implementation.GameSessionImpl;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Created by Igor on 06.12.2016.
 */
public class GameSessionFactory {
    private GameSession gameSession;

    public enum GameSessionImpl {IMPL}
    private static GameSessionImpl impl;

    public static void init(GameSessionImpl impl) {
        GameSessionFactory.impl = impl;
    }


    public GameSession getGameSession() {
        if (gameSession == null)
            switch (impl) {
                case IMPL:
                    gameSession = new com.pesikovlike.kalah.game.session.implementation.GameSessionImpl();
            }
        return gameSession;
    }
}
