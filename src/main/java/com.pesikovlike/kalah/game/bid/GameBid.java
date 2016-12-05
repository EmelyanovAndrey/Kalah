package com.pesikovlike.kalah.game.bid;

import javax.websocket.Session;

/**
 * Created by Igor on 29.11.2016.
 */
public class GameBid {
    private Session sessionOfCreator;
    private Session sessionOfJoined;
    private int holeCount;
    private int stoneCount;
    private String creatorLogin;
    private String friendLogin;

    public GameBid(String creatorLogin, String friendLogin, int holeCount, int stoneCount, Session sessionOfCreator) {
        this.creatorLogin = creatorLogin;
        this.friendLogin = friendLogin;
        this.holeCount = holeCount;
        this.stoneCount = stoneCount;
        this.sessionOfCreator = sessionOfCreator;
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

    public int getHoleCount() {
        return holeCount;
    }

    public void setHoleCount(int holeCount) {
        this.holeCount = holeCount;
    }

    public int getStoneCount() {
        return stoneCount;
    }

    public void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public String getCreatorLogin() {
        return creatorLogin;
    }

    public void setCreatorLogin(String creatorLogin) {
        this.creatorLogin = creatorLogin;
    }

    public String getFriendLogin() {
        return friendLogin;
    }

    public void setFriendLogin(String friendLogin) {
        this.friendLogin = friendLogin;
    }
}
