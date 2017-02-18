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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor on 07.11.2016.
 */
@Singleton
public class GameSessionsServiceImpl implements GameSessionService {
    private static final Logger LOGGER = Logger.getLogger("gameWS Servlet");
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
        for (int i = 0; i < (holeCount + 1) * 2; i++) {
            Hole hole = new Hole();
            hole.setNumber(i);
            if(i == holeCount || i == holeCount * 2 + 1) {
                hole.setStoneCount(0);
            } else {
                hole.setStoneCount(stoneCount);
            }
            hole.setGameState(gameState);
            holes.add(hole);
        }
        gameState.setHoles(holes);
        return gameState;
    }

    private boolean randomPriority() {//true - ходит создатель, false - джойнер
        Random rand = new Random();
        return rand.nextBoolean();
    }


    public GameSession addGameSession(GameBid bid) {
        GameState gameState = initGameState(bid.getCreatorLogin(), bid.getFriendLogin(),
                bid.getHoleCount(), bid.getStoneCount());

        GameSession gameSession = gameSessionFactory.getGameSession();
        gameSession.setSessionOfJoined(bid.getSessionOfJoined());
        gameSession.setSessionOfCreator(bid.getSessionOfCreator());
        gameSession.setGameState(gameState);

        gameSessions.put(bid.getCreatorLogin(), gameSession);
        LOGGER.log(Level.SEVERE, "GameSessionServiceAdd: " + gameSession.getGameState().getInitialHoleCount());
        LOGGER.log(Level.SEVERE, "Count of sessins: " + gameSessions.size());
        return gameSession;
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

    public int makeStep(String creatorLogin, int index) {
        GameSession ses = gameSessions.get(creatorLogin);
        GameState gs = ses.getGameState();
        int numHoles = gs.getInitialHoleCount();
        Hole[] holes = new Hole[(numHoles + 1) * 2];
        holes = ses.getHolesArray();

        int[] ownSide = new int[numHoles];
        int ownKalah;
        int[] otherSide = new int[numHoles];
        int otherKalah;

        boolean prior = gs.getPriority();

        if (prior) {
            for (int i = 0; i < numHoles; i++) {
                ownSide[i] = holes[i].getStoneCount();
                otherSide[i] = holes[i + 1 + numHoles].getStoneCount();
            }
            ownKalah = holes[numHoles].getStoneCount();
            otherKalah = holes[holes.length - 1].getStoneCount();
        } else {
            index -= numHoles + 1;
            for (int i = 0; i < numHoles; i++) {
                ownSide[i] = holes[i + 1 + numHoles].getStoneCount();
                otherSide[i] = holes[i].getStoneCount();
            }
            ownKalah = holes[holes.length - 1].getStoneCount();
            otherKalah = holes[numHoles].getStoneCount();
        }

        int numToSow = ownSide[index];
        ownSide[index] = 0;

        for (int i = index + 1; i < numHoles; i++) {
            numToSow--;
            ownSide[i]++;
            if (numToSow == 0) {
                makeChange(ses, ownSide, ownKalah, otherSide, otherKalah);
                return 0;
            }
        }

        while (numToSow != 0) {
            ownKalah++;
            numToSow--;

            if (numToSow == 0) {
                makeChange(ses, ownSide, ownKalah, otherSide, otherKalah);
                return 0;
            }

            for (int i = 0; i < numHoles; i++) {
                numToSow--;
                otherSide[i]++;
                if (numToSow == 0) {
                    if (otherSide[i] == 2 || otherSide[i] == 3) {
                        while (i >= 0 && (otherSide[i] == 2 || otherSide[i] == 3)) {
                            ownKalah += otherSide[i];
                            otherSide[i] = 0;
                            i--;
                        }
                    }
                    makeChange(ses, ownSide, ownKalah, otherSide, otherKalah);
                    return 0;

                }
            }
            for (int i = 0; i < numHoles; i++) {
                numToSow--;
                ownSide[i]++;
                if (numToSow == 0) {
                    if (ownSide[i] != 1) {
                        numToSow = ownSide[i];
                        ownSide[i] = 0;
                        if (i + numToSow - 1 < numHoles) {
                            for (i++; i < numHoles; i++) {
                                numToSow--;
                                ownSide[i]++;
                                if (numToSow == 0) {
                                    makeChange(ses, ownSide, ownKalah, otherSide, otherKalah);
                                    return 0;
                                }
                            }
                        }
                    } else {
                        makeChange(ses, ownSide, ownKalah, otherSide, otherKalah);
                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    private void makeChange(GameSession ses, int[] ownSide, int ownKalah, int[] otherSide, int otherKalah) {
        GameState gs = ses.getGameState();
        int numHoles = gs.getInitialHoleCount();
        Hole[] holes = new Hole[(numHoles + 1) * 2];
        holes = ses.getHolesArray();
        if (gs.getPriority()) {
            for (int i = 0; i < numHoles; i++) {
                holes[i].setStoneCount(ownSide[i]);
                holes[i + 1 + numHoles].setStoneCount(otherSide[i]);
            }
            holes[numHoles].setStoneCount(ownKalah);
            holes[holes.length - 1].setStoneCount(otherKalah);
        } else {
            for (int i = 0; i < numHoles; i++) {
                holes[i + 1 + numHoles].setStoneCount(ownSide[i]);
                holes[i].setStoneCount(otherSide[i]);
            }
            holes[holes.length - 1].setStoneCount(ownKalah);
            holes[numHoles].setStoneCount(otherKalah);
        }
        gs.setPriority(!gs.getPriority());
    }

    private int findWinner(String creatorLogin) {
        //TODO: проверить, выиграл ли кто-то

        return 0;
    }
}