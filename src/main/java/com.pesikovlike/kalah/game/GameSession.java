package com.pesikovlike.kalah.game;

import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;
import com.pesikovlike.kalah.model.entity.User;

import javax.websocket.Session;
import java.util.Date;
import java.util.Set;

/**
 * Created by Igor on 09.11.2016.
 */
public class GameSession {
    private Session session1;
    private Session session2;
    private GameState gameState;

    public Session getSession1() {
        return session1;
    }

    public void setSession1(Session session1) {
        this.session1 = session1;
    }

    public Session getSession2() {
        return session2;
    }

    public void setSession2(Session session2) {
        this.session2 = session2;
    }

    public long getId() {
        return gameState.getGameStateId();
    }

    public Date getLastSaveDate() {
        return gameState.getLastSaveDate();
    }

    public void setLastSaveDate(Date lastSaveDate) {
        gameState.setLastSaveDate(lastSaveDate);
    }

    public int getStoneCountOfUser1() {
        return gameState.getStoneCountOfUser1();
    }

    public void setStoneCountOfUser1(int stoneCountOfUser1) {
        gameState.setStoneCountOfUser1(stoneCountOfUser1);
    }

    public int getStoneCountOfUser2() {
        return gameState.getStoneCountOfUser2();
    }

    public void setStoneCountOfUser2(int stoneCountOfUser2) {
        gameState.setStoneCountOfUser2(stoneCountOfUser2);
    }

    public int getInitialHoleCount() {
        return gameState.getInitialHoleCount();
    }

    public void setInitialHoleCount(int initialHoleCount) {
        gameState.setInitialStoneCount(initialHoleCount);
    }

    public int getInitialStoneCount() {
        return gameState.getInitialStoneCount();
    }

    public void setInitialStoneCount(int initialStoneCount) {
        gameState.setInitialStoneCount(initialStoneCount);
    }

    public boolean getPriority() {
        return gameState.getPriority();
    }

    public void setPriority(boolean priority) {
        gameState.setPriority(priority);
    }

    public User getUser1() {
        return gameState.getUser1();
    }

    public void setUser1(User user1) {
        gameState.setUser1(user1);
    }

    public User getUser2() {
        return gameState.getUser2();
    }

    public void setUser2(User user2) {
        gameState.setUser2(user2);
    }

    public Set<Hole> getHoles() {
        return gameState.getHoles();
    }

    public void setHoles(Set<Hole> holes) {
        gameState.setHoles(holes);
    }
}
