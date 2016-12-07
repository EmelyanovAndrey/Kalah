package com.pesikovlike.kalah.game.bid;

import javax.websocket.Session;

/**
 * Created by Igor on 06.12.2016.
 */
public interface GameBid {
    public Session getSessionOfCreator();

    public void setSessionOfCreator(Session sessionOfCreator);

    public Session getSessionOfJoined();

    public void setSessionOfJoined(Session sessionOfJoined);

    public int getHoleCount();

    public void setHoleCount(int holeCount);

    public int getStoneCount();

    public void setStoneCount(int stoneCount);

    public String getCreatorLogin();

    public void setCreatorLogin(String creatorLogin);

    public String getFriendLogin();

    public void setFriendLogin(String friendLogin);
}
