package com.pesikovlike.kalah.game.bid;


import javax.websocket.Session;
import java.util.Set;

/**
 * Created by Igor on 06.12.2016.
 */
public interface GameBidService {

    public int addBid(String creatorLogin, String friendLogin, int holeCount, int stoneCount, Session sessionOfCreator);

    public int deleteBid(String creatorLogin);

    public GameBid getBid(String creatorLogin);

    public Set<GameBid> getAllGameBidsForUser(String friendLogin);
}
