package com.pesikovlike.kalah.game.bid.implementation;

import com.pesikovlike.kalah.game.bid.GameBid;

import javax.websocket.Session;

/**
 * Created by Igor on 29.11.2016.
 */
public class GameBidImpl implements GameBid{
    private Session sessionOfCreator;
    private Session sessionOfJoined;
    private int holeCount;
    private int stoneCount;
    private String creatorLogin;
    private String friendLogin;
    private boolean block;

    public GameBidImpl() {}
    public GameBidImpl(String creatorLogin, String friendLogin, int holeCount, int stoneCount, Session sessionOfCreator) {
        this.creatorLogin = creatorLogin;
        this.friendLogin = friendLogin;
        this.holeCount = holeCount;
        this.stoneCount = stoneCount;
        this.sessionOfCreator = sessionOfCreator;
        this.block = false;
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

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

}
