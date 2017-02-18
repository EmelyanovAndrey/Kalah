package com.pesikovlike.kalah.game.session.implementation;

import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;

import javax.websocket.Session;

/**
 * Created by Igor on 09.11.2016.
 */
public class GameSessionImpl implements GameSession {
    private Session sessionOfCreator;
    private Session sessionOfJoined;
    private GameState gameState;


    public GameSessionImpl() {
    }

    public GameSessionImpl(Session sessionOfCreator, Session sessionOfJoined, GameState gameState) {
        this.sessionOfCreator = sessionOfCreator;
        this.sessionOfJoined = sessionOfJoined;
        this.gameState = gameState;
    }

    public String getInfo() {
        String res = "board: ";
        int numHoles = (gameState.getInitialHoleCount() + 1) * 2;
        Hole[] holes = new Hole[numHoles];
        holes = getHolesArray();
        for (int i = 0; i < numHoles; i++) {
            res += holes[i].getStoneCount() + " ";
        }
        return res;
    }

    public Hole[] getHolesArray() {
        int numHoles = (gameState.getInitialHoleCount() + 1) * 2;
        Hole[] holes = new Hole[numHoles];
        Hole[] holesSet = new Hole[numHoles];
        holesSet = gameState.getHoles().toArray(holesSet);
        for (int i = 0; i < numHoles; i++) {
            for (int j = 0; j < numHoles; j++) {
                if (holesSet[j].getNumber() == i) {
                    holes[i] = holesSet[j];
                }
            }
        }
        return holes;
    }

    public Session getSessionOfCreator() {
        return sessionOfCreator;
    }

    public void setSessionOfCreator(Session sessionOfCreator) {
        this.sessionOfCreator = sessionOfCreator;
    }

    public Session getSessionOfJoined() {
        return sessionOfJoined;
    }

    public void setSessionOfJoined(Session sessionOfJoined) {
        this.sessionOfJoined = sessionOfJoined;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
