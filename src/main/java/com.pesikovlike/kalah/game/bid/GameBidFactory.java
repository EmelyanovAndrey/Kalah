package com.pesikovlike.kalah.game.bid;

import com.pesikovlike.kalah.game.bid.implementation.GameBidImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Created by Igor on 06.12.2016.
 */
public class GameBidFactory {
    private GameBid gameBid;

    public enum GameBidImpl {IMPL}
    private static GameBidImpl impl;

    public static void init(GameBidImpl impl) {
        GameBidFactory.impl = impl;
    }

    public GameBid getGameBid() {
        if (gameBid == null)
            switch (impl) {
                case IMPL:
                    gameBid = new com.pesikovlike.kalah.game.bid.implementation.GameBidImpl();
            }
        return gameBid;
    }
}
