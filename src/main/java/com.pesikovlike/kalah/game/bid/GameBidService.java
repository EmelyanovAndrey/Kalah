package com.pesikovlike.kalah.game.bid;

import com.pesikovlike.kalah.model.dao.interfaces.UserDAO;
import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 29.11.2016.
 */
@Singleton
public class GameBidService {

    @EJB
    private UserService userService;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    private final Map<String, GameBid> gameBids = new HashMap<String, GameBid>();

    public int addBid(String creatorLogin, String friendLogin, int holeCount, int stoneCount, Session sessionOfCreator) {
        GameBid newBid = new GameBid(creatorLogin, friendLogin, holeCount, stoneCount, sessionOfCreator);
        gameBids.put(creatorLogin, newBid);
        return 0;
    }

    public int deleteBid(String creatorLogin) {
        gameBids.remove(creatorLogin);
        return 0;
    }

    public GameBid getBid(String creatorLogin) {
        return gameBids.get(creatorLogin);
    }

    public Set<GameBid> getAllGameBidsForUser(String friendLogin) {
        Set<GameBid> bids = new HashSet<GameBid>();
        for(Map.Entry<String, GameBid> bid : gameBids.entrySet()) {
            GameBid value = bid.getValue();
            if (value.getFriendLogin() == null || value.getFriendLogin().equals(friendLogin)) {
                bids.add(value);
            }
        }
        return bids;
    }
}
