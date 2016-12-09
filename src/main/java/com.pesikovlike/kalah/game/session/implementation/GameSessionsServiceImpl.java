package com.pesikovlike.kalah.game.session.implementation;

import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.game.session.GameSessionFactory;
import com.pesikovlike.kalah.game.session.GameSessionService;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;
import com.pesikovlike.kalah.user.UserService;


import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by Igor on 07.11.2016.
 */
@Singleton
public class GameSessionsServiceImpl implements GameSessionService {

    @EJB
    private UserService userService;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    @Inject
    private GameSessionFactory gameSessionFactory;

    private final Map<String, GameSession> gameSessions = new HashMap<String, GameSession>();


    private GameState initGameState(String creatorLogin, String joinedLogin,
                                    int holeCount, int stoneCount) {
        GameState gameState = new GameState();
        gameState.setUser1(userDAO.getUserByLogin(creatorLogin));
        gameState.setUser2(userDAO.getUserByLogin(joinedLogin));
        gameState.setInitialHoleCount(holeCount);
        gameState.setInitialStoneCount(stoneCount);
        gameState.setPriority(randomPriority());
        gameState.setStoneCountOfUser1(0);
        gameState.setStoneCountOfUser2(0);
        Set<Hole> holes = new HashSet<Hole>(holeCount);
        for (int i = 0; i < holeCount; i++) {
            Hole hole = new Hole();
            hole.setNumber(i);
            hole.setStoneCount(stoneCount);
            holes.add(hole);
        }
        gameState.setHoles(holes);
        return gameState;
    }

    private boolean randomPriority() {//true - ходит создатель, false - джойнер
        Random rand = new Random();
        return rand.nextBoolean();
    }


    public int addGameSession(GameBid bid) {
        GameState gameState = initGameState(bid.getCreatorLogin(), bid.getFriendLogin(),
                bid.getHoleCount(), bid.getStoneCount());

        GameSession gameSession = gameSessionFactory.getGameSession();
        gameSession.setSessionOfJoined(bid.getSessionOfJoined());
        gameSession.setGameState(gameState);

        gameSessions.put(bid.getCreatorLogin(), gameSession);

        return 0;
    }

    public GameSession getGameSession(String creatorLogin) {
        return gameSessions.get(creatorLogin);
    }

    public Map<String, GameSession> getAllGameSessions() {
        return gameSessions;
    }

    public int deleteGameSession(String creatorLogin) {
        gameSessions.remove(creatorLogin);
        return 0;
    }

    public int makeStep(String creatorLogin, int player) {
        //TODO: сделать ход
        return 0;
    }

    private int findWinner(String creatorLogin) {
        //TODO: проверить, выиграл ли кто-то

        return 0;
    }
}
