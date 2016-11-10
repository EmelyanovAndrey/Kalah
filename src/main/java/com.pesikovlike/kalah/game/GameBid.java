package com.pesikovlike.kalah.game;

import javax.websocket.Session;

/**
 * Created by Igor on 09.11.2016.
 */
public class GameBid {

    private String frendLogin;
    private int initialHoleCount;
    private int initialStoneCount;


    public String getFrendLogin() {
        return frendLogin;
    }

    public void setFrendLogin(String frendLogin) {
        this.frendLogin = frendLogin;
    }

    public int getInitialHoleCount() {
        return initialHoleCount;
    }

    public void setInitialHoleCount(int initialHoleCount) {
        this.initialHoleCount = initialHoleCount;
    }

    public int getInitialStoneCount() {
        return initialStoneCount;
    }

    public void setInitialStoneCount(int initialStoneCount) {
        this.initialStoneCount = initialStoneCount;
    }
}
