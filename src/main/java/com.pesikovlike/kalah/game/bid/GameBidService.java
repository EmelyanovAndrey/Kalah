package com.pesikovlike.kalah.game.bid;


import javax.websocket.Session;
import java.util.Set;

/**
 * Created by Igor on 06.12.2016.
 */
public interface GameBidService {

    public int addBid(GameBid newBid);

    public int deleteBid(String creatorLogin);

    public GameBid getBid(String creatorLogin);

    public Set<GameBid> getAllGameBidsForUser(String friendLogin);
}
